package com.iprogrammerr.foodcontroller.model.day

import android.database.Cursor
import com.iprogrammerr.foodcontroller.database.Database
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
        val cursor = this.database.query(sql.toString())
        cursor.moveToNext()
        val day = day(cursor)
        cursor.close()
        return day
    }

    private fun day(cursor: Cursor): Day {
        val meals: MutableList<Meal> = ArrayList()
        do {
            var moved = false
            val mealId = cursor.getLong(cursor.getColumnIndex("m.id"))
            val time = cursor.getLong(cursor.getColumnIndex("m.time"))
            val food: MutableList<Food> = ArrayList()
            do {
                if (cursor.getLong(cursor.getColumnIndex("m.id")) != mealId) {
                    meals.add(DatabaseMeal(mealId, this.database, time, food))
                    break
                }
                food.add(
                    DatabaseFood(
                        cursor.getLong(cursor.getColumnIndex("f.id")), this.database,
                        cursor.getString(cursor.getColumnIndex("fd.name")),
                        cursor.getInt(cursor.getColumnIndex("f.weight")),
                        cursor.getInt(cursor.getColumnIndex("fd.protein")),
                        cursor.getInt(cursor.getColumnIndex("fd.calories"))
                    )
                )
                moved = cursor.moveToNext()
            } while (moved)
        } while (moved)
        return DatabaseDay(
            cursor.getLong(cursor.getColumnIndex("d.id")),
            this.database,
            cursor.getLong(cursor.getColumnIndex("date")),
            cursor.getDouble(cursor.getColumnIndex("weight")),
            cursor.getInt(cursor.getColumnIndex("caloriesGoal")),
            cursor.getInt(cursor.getColumnIndex("proteinGoal")),
            meals
        )
    }

    override fun exists(date: Long): Boolean {
        val cursor = this.database.query("select id from day where date = ${dayStart(date)}")
        val exists = cursor.moveToNext()
        cursor.close()
        return exists
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