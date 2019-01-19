package com.iprogrammerr.foodcontroller.model.history

import com.iprogrammerr.foodcontroller.database.Database
import java.util.*
import kotlin.collections.ArrayList

class DatabaseHistory(private val database: Database) : History {

    override fun years() =
        this.database.query(
            "SELECT MIN(date) min, MAX(date) max from day"
        ).use { rs ->
            val r = rs.next()
            val c = Calendar.getInstance()
            c.timeInMillis = if (r.has("min")) {
                r.long("min")
            } else {
                System.currentTimeMillis()
            }
            val years = ArrayList<Int>()
            years.add(c[Calendar.YEAR])
            c.timeInMillis = if (r.has("max")) {
                r.long("max")
            } else {
                System.currentTimeMillis()
            }
            for (i in 1 until (c[Calendar.YEAR] - years[0])) {
                years.add(years[0] + i)
            }
            years
        }

    override fun months(year: Int): List<Calendar> =
        this.database.query(monthsQuery(year)).use { rs ->
            val r = rs.next()
            val months = ArrayList<Calendar>()
            if (r.has("min") && r.has("max")) {
                val first = Calendar.getInstance()
                first.timeInMillis = r.long("min")
                months.add(first)
                val last = Calendar.getInstance()
                last.timeInMillis = r.long("max")
                val tmp = Calendar.getInstance()
                tmp.timeInMillis = first.timeInMillis
                while (tmp[Calendar.MONTH] < last[Calendar.MONTH]) {
                    tmp.add(Calendar.MONTH, 1)
                    val c = Calendar.getInstance()
                    c.timeInMillis = tmp.timeInMillis
                    months.add(c)
                }
            }
            months
        }

    private fun monthsQuery(year: Int): String {
        val calendar = Calendar.getInstance()
        calendar[Calendar.YEAR] = year
        calendar[Calendar.MONTH] = 0
        calendar[Calendar.DAY_OF_MONTH] = 0
        calendar[Calendar.HOUR_OF_DAY] = 0
        val min = calendar.timeInMillis
        calendar[Calendar.MONTH] = 11
        calendar[Calendar.DAY_OF_MONTH] = 31
        calendar[Calendar.HOUR_OF_DAY] = 23
        return "SELECT MIN(date) min, MAX(date) max FROM day " +
            "WHERE date >= $min AND date <= ${calendar.timeInMillis}"
    }
}