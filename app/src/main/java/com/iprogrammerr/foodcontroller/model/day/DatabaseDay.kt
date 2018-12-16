package com.iprogrammerr.foodcontroller.model.day

import com.iprogrammerr.foodcontroller.database.Database
import com.iprogrammerr.foodcontroller.model.NutritionalValues
import com.iprogrammerr.foodcontroller.model.meal.Meal

class DatabaseDay(private val id: Long, private val database: Database) : Day {

    override fun goals(): NutritionalValues {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun date(): Long {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun meals(): List<Meal> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun removeMeal(id: Long) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}