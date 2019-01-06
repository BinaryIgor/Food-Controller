package com.iprogrammerr.foodcontroller.model.meal

import android.content.ContentValues
import com.iprogrammerr.foodcontroller.database.Database
import com.iprogrammerr.foodcontroller.model.NutritionalValues
import com.iprogrammerr.foodcontroller.model.food.DatabaseFood
import com.iprogrammerr.foodcontroller.model.food.Food
import kotlin.math.roundToInt

class DatabaseMeal(private val id: Long, private val database: Database) :
    Meal {

    constructor(
        id: Long, database: Database, time: Long, food: MutableList<Food>
    ) : this(id, database) {
        this.time = time
        this.food.addAll(food)
        this.loaded = true
    }

    private var time = 0L
    private val food: MutableList<Food> = ArrayList()
    private var loaded = false

    override fun id() = this.id

    override fun time(): Long {
        if (!this.loaded) {
            load()
        }
        return this.time
    }

    override fun changeTime(time: Long) {
        val values = ContentValues()
        values.put("time", time)
        this.database.update("meal", "id = ${this.id}", values)
        this.time = time
    }

    override fun food(): List<Food> {
        if (!this.loaded) {
            load()
        }
        return this.food
    }

    override fun nutritionalValues(): NutritionalValues {
        if (!this.loaded) {
            load()
        }
        var calories = 0.0
        var protein = 0.0
        for (f in this.food) {
            calories += f.calories()
            protein += f.protein()
        }
        if (this.food.size > 0) {
            calories /= this.food.size
            protein /= this.food.size
        }
        return object : NutritionalValues {

            override fun calories() = calories.roundToInt()

            override fun protein() = protein.roundToInt()
        }
    }

    override fun addFood(food: Food) {
        val values = ContentValues()
        values.put("food_id", food.id())
        values.put("meal_id", this.id)
        this.database.insert("food_meal", values)
        this.loaded = false
    }

    override fun removeFood(id: Long) {
        this.database.delete(
            "food_meal", "meal_id = ${this.id} and food_id = $id"
        )
        this.loaded = false
    }

    private fun load() {
        val query =
            StringBuilder().append(
                "SELECT time, f.id as f_id, f.weight, fd.name, fd.protein, fd.calories ")
                .append("FROM meal m INNER JOIN food_meal fm ON m.id = fm.meal_id ")
                .append("INNER JOIN food f ON fm.food_id = f.id ")
                .append("INNER JOIN food_definition fd ON f.definition_id = fd.id ")
                .append("WHERE m.id = ${this.id}")
        this.food.clear()
        this.database.query(query.toString()).use { rs ->
            var r = rs.next()
            this.time = r.long("time")
            while (rs.hasNext()) {
                this.food.add(
                    DatabaseFood(
                        r.long("f_id"),
                        this.database,
                        r.string("name"),
                        r.int("weight"),
                        r.int("protein"),
                        r.int("calories")
                    )
                )
                r = rs.next()
            }
            this.loaded = true
        }
    }
}