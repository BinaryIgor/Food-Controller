package com.iprogrammerr.foodcontroller.model.food

import com.iprogrammerr.foodcontroller.database.Database

class DatabaseFoodDefinitions(private val database: Database) : FoodDefinitions {

    override fun definition(id: Long): FoodDefinition {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun delete(id: Long) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}