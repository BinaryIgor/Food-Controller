package com.iprogrammerr.foodcontroller.model.food

import android.content.ContentValues
import com.iprogrammerr.foodcontroller.database.Database

class DatabaseFoodDefinition(
    private val id: Long,
    private val database: Database,
    private var name: String,
    private var calories: Int,
    private var protein: Double
) : FoodDefinition {

    override fun id() = this.id

    override fun name() = this.name

    override fun calories() = this.calories

    override fun protein() = this.protein

    override fun update(name: String, calories: Int, protein: Double) {
        val values = ContentValues()
        if (this.name != name) {
            values.put("name", name)
        }
        if (this.calories != calories) {
            values.put("calories", calories)
        }
        if (this.protein != protein) {
            values.put("protein", protein)
        }
        if (values.size() > 0) {
            this.database.update("food_definition", "id = ${this.id}", values)
            this.name = name
            this.calories = calories
            this.protein = protein
        }
    }
}