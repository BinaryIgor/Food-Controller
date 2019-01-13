package com.iprogrammerr.foodcontroller.model.food

import android.content.ContentValues
import com.iprogrammerr.foodcontroller.database.Database

class DatabasePortions(private val database: Database) : Portions {

    override fun exists(definitionId: Long, weight: Int) =
        this.database.query(
            "SELECT id FROM food WHERE definition_id = $definitionId AND weight = $weight"
        ).use { rs -> rs.next().has("id") }

    override fun create(definitionId: Long, weight: Int) {
        val values = ContentValues()
        values.put("definition_id", definitionId)
        values.put("weight", weight)
        this.database.insert("food", values)
    }

    override fun add(definitionId: Long, weight: Int, mealId: Long) {
        val id = this.database.query(
            "SELECT id FROM food WHERE definition_id = $definitionId AND weight = $weight"
        ).use { rs -> rs.next().long("id") }
        val values = ContentValues()
        values.put("food_id", id)
        values.put("meal_id", mealId)
        this.database.insert("food_meal", values)
    }

    override fun update(foodId: Long, mealId: Long, definitionId: Long, weight: Int) {
        val values = ContentValues()
        values.put(
            "food_id", this.database.query(
                StringBuilder()
                    .append("SELECT f.id FROM food f ")
                    .append("INNER JOIN food_definition ON f.definition_id = $definitionId ")
                    .append("WHERE weight = $weight")
                    .toString()
            ).use { rs ->
                rs.next().long("id")
            }
        )
        println("Id = ${values.getAsLong("food_id")}")
        this.database.update("food_meal", "food_id = $foodId AND meal_id = $mealId", values)
    }
}