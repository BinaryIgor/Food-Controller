package com.iprogrammerr.foodcontroller.model.day

import com.iprogrammerr.foodcontroller.database.Database
import com.iprogrammerr.foodcontroller.model.NutritionalValues

class DatabaseDays(private val database: Database) : Days {

    override fun range(from: String, to: String): List<Day> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun day(date: String): Day {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun exists(date: String): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun create(goals: NutritionalValues) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}