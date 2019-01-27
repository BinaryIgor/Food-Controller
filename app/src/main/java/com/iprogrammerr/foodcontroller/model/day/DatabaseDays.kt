package com.iprogrammerr.foodcontroller.model.day

import android.content.ContentValues
import com.iprogrammerr.foodcontroller.database.Database
import com.iprogrammerr.foodcontroller.database.Rows
import com.iprogrammerr.foodcontroller.model.food.ConstantFood
import com.iprogrammerr.foodcontroller.model.food.Food
import com.iprogrammerr.foodcontroller.model.meal.DatabaseMeal
import com.iprogrammerr.foodcontroller.model.meal.Meal
import java.util.*
import kotlin.collections.ArrayList

class DatabaseDays(private val database: Database) : Days {

    override fun range(from: Long, to: Long): List<Day> {
        return this.database.query(
            daysQuery(from, to)
        ).use { rs ->
            val days = ArrayList<Day>()
            rs.next()
            do {
                val day = day(rs)
                days.add(day)
            } while (rs.current().long("d_id") != day.id())
            days
        }
    }

    private fun daysQuery(start: Long, end: Long) =
        StringBuilder(
            "SELECT d.id as d_id, d.date, d.weight as d_weight, d.calories_goal, d.protein_goal, "
        ).append("m.id as m_id, m.time, f.id as f_id, f.weight as f_weight, ")
            .append("fd.id as fd_id, fd.name, fd.calories, fd.protein ")
            .append("FROM day d LEFT JOIN meal m ON d.id = m.day_id ")
            .append("LEFT JOIN food_meal fm ON m.id = fm.meal_id ")
            .append("LEFT JOIN food f ON fm.food_id = f.id ")
            .append("LEFT JOIN food_definition fd ON f.definition_id = fd.id ")
            .append("WHERE date >= ${dayStart(start)} AND date <= ${dayEnd(end)} ")
            .append("ORDER BY date ASC")
            .toString()

    override fun day(date: Long): Day {
        return this.database.query(
            daysQuery(date, date)
        ).use { r ->
            r.next()
            day(r)
        }
    }

    private fun day(rows: Rows): Day {
        var row = rows.current()
        val id = row.long("d_id")
        val date = row.long("date")
        val weight = row.double("d_weight")
        val caloriesGoal = row.int("calories_goal")
        val proteinGoal = row.int("protein_goal")
        val meals: MutableList<Meal> = ArrayList()
        if (row.has("f_id")) {
            do {
                val meal = meal(rows)
                meals.add(meal)
                row = rows.current()
            } while (row.long("d_id") == id && meal.id() != row.long("m_id"))
        }
        return DatabaseDay(id, this.database, date, weight, caloriesGoal, proteinGoal, meals)
    }

    private fun meal(rows: Rows): Meal {
        val food: MutableList<Food> = ArrayList()
        var row = rows.current()
        val id = row.long("m_id")
        val time = row.long("time")
        do {
            val mealId = row.long("m_id")
            if (mealId != id) {
                break
            }
            food.add(
                ConstantFood(
                    row.long("f_id"), row.string("name"), row.int("f_weight"),
                    row.int("calories"), row.double("protein"), row.long("fd_id")
                )
            )
            if (!rows.hasNext()) {
                break
            }
            row = rows.next()
        } while (true)
        return DatabaseMeal(id, this.database, time, food)
    }

    override fun exists(date: Long) = this.database
        .query("SELECT id FROM day WHERE date >= ${dayStart(date)} AND date <= ${dayEnd(date)}")
        .use { r -> r.next().has("id") }

    override fun create(weight: Double, caloriesGoal: Int, proteinGoal: Int) {
        val values = ContentValues()
        values.put("weight", weight)
        values.put("date", System.currentTimeMillis())
        values.put("calories_goal", caloriesGoal)
        values.put("protein_goal", proteinGoal)
        this.database.insert("day", values)
    }

    override fun delete(id: Long) {
        this.database.delete("day", "id = $id")
    }

    private fun dayStart(date: Long) =
        dayWithOffset(date, 0, 0, 0, 0)

    private fun dayEnd(date: Long) =
        dayWithOffset(date, 23, 59, 59, 999)

    private fun dayWithOffset(
        date: Long, hour: Int, minute: Int, second: Int,
        millisecond: Int
    ): Long {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = date
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, second)
        calendar.set(Calendar.MILLISECOND, millisecond)
        return calendar.timeInMillis
    }
}