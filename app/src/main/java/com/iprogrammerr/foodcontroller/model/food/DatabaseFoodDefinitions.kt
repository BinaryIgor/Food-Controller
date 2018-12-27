package com.iprogrammerr.foodcontroller.model.food

import android.content.ContentValues
import com.iprogrammerr.foodcontroller.database.Database

class DatabaseFoodDefinitions(private val database: Database) : FoodDefinitions {

    override fun create(name: String, calories: Int, protein: Double, categoryId: Long) {
        val values = ContentValues()
        values.put("name", name)
        values.put("calories", calories)
        values.put("protein", protein)
        values.put("category_id", categoryId)
        this.database.insert("food_definition", values)
    }

    override fun definition(id: Long): FoodDefinition {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun delete(id: Long) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}