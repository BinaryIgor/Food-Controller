package com.iprogrammerr.foodcontroller.model.day

import android.content.ContentValues
import com.iprogrammerr.foodcontroller.database.Database
import com.iprogrammerr.foodcontroller.database.Rows
import com.iprogrammerr.foodcontroller.model.NutritionalValues
import com.iprogrammerr.foodcontroller.model.food.DatabaseFood
import com.iprogrammerr.foodcontroller.model.food.Food
import com.iprogrammerr.foodcontroller.model.meal.DatabaseMeal
import com.iprogrammerr.foodcontroller.model.meal.Meal
import java.util.*
import kotlin.collections.ArrayList

class DatabaseDays(private val database: Database) : Days {

    override fun range(from: Long, to: Long): List<Day> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun day(date: Long): Day {
        val sql = StringBuilder()
            .append("SELECT * FROM day d INNER JOIN meal m ON d.id = m.day_id ")
            .append("INNER JOIN food_meal fm ON m.id = fm.meal_id ")
            .append("INNER JOIN food f ON fm.food_id = f.id ")
            .append("INNER JOIN food_definition fd ON f.definition_id = fd.id ")
            .append("WHERE date ")
            .append(">= ${dayStart(date)} AND date <= ${dayEnd(date)} LIMIT 1")
        this.database.query(sql.toString()).use { r ->
            return day(r)
        }
    }

    private fun day(rows: Rows): Day {
        val meals: MutableList<Meal> = ArrayList()
        var row = rows.next()
        do {
            var moved = false
            val mealId = row.long("m.id")
            val time = row.long("m.time")
            val food: MutableList<Food> = ArrayList()
            do {
                if (row.long("m.id") != mealId) {
                    meals.add(DatabaseMeal(mealId, this.database, time, food))
                    break
                }
                food.add(
                    DatabaseFood(
                        row.long("f.id"), this.database, row.string("fd.name"),
                        row.int("f.weight"), row.int("fd.protein"),
                        row.int("fd.calories")
                    )
                )
                row = rows.next()
                moved = rows.hasNext()
            } while (moved)
        } while (moved)
        return DatabaseDay(
            row.long("d.id"),
            this.database, row.long("date"), row.double("weight"),
            row.int("caloriesGoal"), row.int("proteinGoal"), meals
        )
    }

    override fun exists(date: Long) = this.database
        .query("SELECT id FROM day WHERE date >= ${dayStart(date)} AND date <= ${dayEnd(date)}")
        .use { r -> !r.next().isEmpty() }

    override fun create(weight: Double, goals: NutritionalValues) {
        val values = ContentValues()
        values.put("weight", weight)
        values.put("date", System.currentTimeMillis())
        values.put("calories_goal", goals.calories())
        values.put("protein_goal", goals.protein())
        this.database.insert("day", values)
    }

    private fun dayStart(date: Long) =
        dayWithOffset(date, 0, 0, 0, 0)

    private fun dayEnd(date: Long) =
        dayWithOffset(date, 23, 59, 59, 999)

    private fun dayWithOffset(date: Long, hour: Int, minute: Int, second: Int, millisecond: Int): Long {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = date
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, second)
        calendar.set(Calendar.MILLISECOND, millisecond)
        return calendar.timeInMillis
    }
}