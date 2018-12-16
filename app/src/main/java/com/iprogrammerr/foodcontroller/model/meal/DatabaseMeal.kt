package com.iprogrammerr.foodcontroller.model.meal

import com.iprogrammerr.foodcontroller.database.Database
import com.iprogrammerr.foodcontroller.model.NutritionalValues
import com.iprogrammerr.foodcontroller.model.food.Food

class DatabaseMeal(private val id: Long, private val database: Database) : Meal {

    override fun id() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun hourMinutes(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun food(): List<Food> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun nutritionalValues(): NutritionalValues {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun removeFood(id: Long) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}