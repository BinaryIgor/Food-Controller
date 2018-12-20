package com.iprogrammerr.foodcontroller.model.day

import com.iprogrammerr.foodcontroller.database.Database
import com.iprogrammerr.foodcontroller.model.NutritionalValues
import java.lang.StringBuilder
import java.text.DateFormat

class DatabaseDays(private val database: Database, private val format: DateFormat) : Days {

    override fun range(from: String, to: String): List<Day> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun day(date: String): Day {
        var day: Day
        val sql: StringBuilder = StringBuilder()
        val cursor = this.database.query(sql.toString())
        return DatabaseDay(1, this.database)
    }

    override fun exists(date: String): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun create(goals: NutritionalValues) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}