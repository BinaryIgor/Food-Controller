package com.iprogrammerr.foodcontroller.model.meal

import android.content.ContentValues
import com.iprogrammerr.foodcontroller.database.Database
import com.iprogrammerr.foodcontroller.model.food.ConstantFood
import com.iprogrammerr.foodcontroller.model.food.Food

class DatabaseMeals(private val database: Database) : Meals {

    override fun all(): List<Meal> {
        TODO(
            "not implemented"
        ) //To change body of created functions use File | Settings | File Templates.
    }

    override fun meal(id: Long): Meal {
        return this.database.query(
            StringBuilder("SELECT m.id as m_id, time, f.id as f_id, f.weight, fd.id as fd_id, fd.name, fd.protein, fd.calories ")
                .append("FROM meal m LEFT JOIN food_meal fm ON m.id = fm.meal_id ")
                .append("LEFT JOIN food f ON fm.food_id = f.id ")
                .append("LEFT JOIN food_definition fd ON f.definition_id = fd.id ")
                .append("WHERE m.id = $id")
                .toString()
        ).use { rs ->
            val food: MutableList<Food> = ArrayList()
            var r = rs.next()
            if (r.has("f_id")) {
                do {
                    food.add(
                        ConstantFood(
                            r.long("f_id"),
                            r.string("name"),
                            r.int("weight"),
                            r.int("calories"),
                            r.double("protein"),
                            r.long("fd_id")
                        )
                    )
                    if (!rs.hasNext()) {
                        break
                    }
                    r = rs.next()
                } while (true)
            }
            DatabaseMeal(id, this.database, r.long("time"), food)
        }
    }

    override fun create(time: Long, dayId: Long): Long {
        val values = ContentValues()
        values.put("time", time)
        values.put("day_id", dayId)
        return this.database.insert("meal", values)
    }

    override fun delete(id: Long) {
        this.database.delete("meal", "id = $id")
    }
}