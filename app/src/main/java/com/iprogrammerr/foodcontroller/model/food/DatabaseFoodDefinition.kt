package com.iprogrammerr.foodcontroller.model.food

import com.iprogrammerr.foodcontroller.database.Database

class DatabaseFoodDefinition(private val id: Long, private val categoryId: Long, private val database: Database) :
    FoodDefinition {

    override fun name(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun calories(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun protein(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun update(values: Map<String, Any>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}