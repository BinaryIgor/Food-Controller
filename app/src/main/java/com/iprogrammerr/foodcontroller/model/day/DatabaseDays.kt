package com.iprogrammerr.foodcontroller.model.day

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
            .append("select * from day d inner join meal m on d.id = m.day_id ")
            .append("inner join food_meal fm on m.id = fm.meal_id ")
            .append("inner join food f on fm.food_id = f.id ")
            .append("inner join food_definition fd on f.definition_id = fd.id ")
            .append("where date ")
            .append(">= ${dayStart(date)} and date <= ${dayEnd(date)} limit 1")
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
        .query("select id from day where date = ${dayStart(date)}")
        .use { r ->
            r.hasNext()
        }


    override fun create(goals: NutritionalValues, weight: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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