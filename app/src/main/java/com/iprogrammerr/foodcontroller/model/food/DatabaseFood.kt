package com.iprogrammerr.foodcontroller.model.food

import com.iprogrammerr.foodcontroller.database.Database
import com.iprogrammerr.foodcontroller.model.NutritionalValues
import kotlin.math.roundToInt

class DatabaseFood(
    private val id: Long,
    private val database: Database
) : Food {

    private val attributes: MutableMap<String, Any> = HashMap()

    constructor(id: Long, database: Database, name: String, weight: Int, calories: Int,
        protein: Double, definitionId: Long) : this(
        id, database
    ) {
        this.attributes["name"] = name
        this.attributes["weight"] = weight
        this.attributes["calories"] = calories
        this.attributes["protein"] = protein
        this.attributes["definitionId"] = definitionId
    }

    override fun id() = this.id

    override fun name(): String {
        if (!this.attributes.contains("name")) {
            load()
        }
        return this.attributes["name"] as String
    }

    override fun weight(): Int {
        if (!this.attributes.contains("weight")) {
            load()
        }
        return this.attributes["weight"] as Int
    }

    override fun values(): NutritionalValues {
        if (!this.attributes.contains("calories") || !this.attributes.contains("protein")) {
            load()
        }
        return object : NutritionalValues {

            override fun calories() =
                ((weight() / 100.0) * (this@DatabaseFood.attributes["calories"] as Int)).roundToInt()

            override fun protein() =
                (weight() / 100.0) * this@DatabaseFood.attributes["protein"] as Double
        }
    }

    override fun definition(): FoodDefinition {
        if (!this.attributes.contains("definitionId")) {
            load()
        }
        return DatabaseFoodDefinition(this.attributes["definitionId"] as Long, this.database)
    }

    private fun load() {
        this.database.query(
            "SELECT * FROM food INNER JOIN food_definition ON food.definition_id = food_definition.id WHERE id = ${this.id}"
        ).use { rs ->
            val row = rs.next()
            this.attributes["name"] = row.long("name")
            this.attributes["weight"] = row.int("weight")
            this.attributes["calories"] = row.int("calories")
            this.attributes["protein"] = row.double("protein")
            this.attributes["definitionId"] = row.long("definition_id")
        }
    }
}