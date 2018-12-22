package com.iprogrammerr.foodcontroller.model.meal

import android.content.ContentValues
import com.iprogrammerr.foodcontroller.database.Database
import com.iprogrammerr.foodcontroller.model.NutritionalValues
import com.iprogrammerr.foodcontroller.model.food.DatabaseFood
import com.iprogrammerr.foodcontroller.model.food.Food
import kotlin.math.roundToInt

class DatabaseMeal(private val id: Long, private val database: Database) : Meal {

    constructor(id: Long, database: Database, time: Long, food: MutableList<Food>) : this(id, database) {
        this.time = time
        this.food.addAll(food)
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
        calories /= this.food.size
        protein /= this.food.size
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
        this.database.delete("food_meal", "meal_id = ${this.id} and food_id = $id")
        this.loaded = false
    }

    private fun load() {
        val query = StringBuilder()
            .append("select * from meal m inner join food_meal fm on m.id = fm.meal_id ")
            .append("inner join food f on fm.food_id = f.id")
            .append("inner join food_definition fd on f.definition_id = fd.id ")
            .append("where id = ${this.id}")
        val cursor = this.database.query(query.toString())
        this.food.clear()
        while (cursor.moveToNext()) {
            val attributes: MutableMap<String, Any> = mutableMapOf(
                "name" to cursor.getString(cursor.getColumnIndex("fd.name")),
                "weight" to cursor.getInt(cursor.getColumnIndex("f.weight")),
                "protein" to cursor.getInt(cursor.getColumnIndex("fd.protein")),
                "calories" to cursor.getInt(cursor.getColumnIndex("fd.calories"))
            )
            this.food.add(DatabaseFood(cursor.getLong(cursor.getColumnIndex("f.id")), this.database, attributes))
        }
        this.time = cursor.getLong(cursor.getColumnIndex("m.time"))
        this.loaded = true
    }
}