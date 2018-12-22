package com.iprogrammerr.foodcontroller.model.day

import com.iprogrammerr.foodcontroller.database.Database
import com.iprogrammerr.foodcontroller.model.NutritionalValues
import java.util.*

class DatabaseDays(private val database: Database) : Days {

    override fun range(from: Long, to: Long): List<Day> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun day(date: Long): Day {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun exists(date: Long): Boolean {
        val cursor = this.database.query("select id from day where date = ${dayStart(date)}")
        val exists = cursor.moveToNext()
        cursor.close()
        return exists
    }

    override fun create(goals: NutritionalValues) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun dayStart(date: Long): Long {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = date
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }
}