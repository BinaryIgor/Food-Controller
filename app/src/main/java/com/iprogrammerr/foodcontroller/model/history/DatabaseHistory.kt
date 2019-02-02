package com.iprogrammerr.foodcontroller.model.history

import com.iprogrammerr.foodcontroller.database.Database
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class DatabaseHistory(private val database: Database) : History {

    override fun years() =
        this.database.query(
            "SELECT MIN(date) min, MAX(date) max from day"
        ).use { rs ->
            val years = ArrayList<Int>()
            val r = rs.next()
            if (r.has("min") && r.has("max")) {
                val c = Calendar.getInstance()
                c.timeInMillis = r.long("min")
                if (hasYearHistory(c.get(Calendar.YEAR))) {
                    years.add(c[Calendar.YEAR])
                }
                if (years.isNotEmpty()) {
                    c.timeInMillis = r.long("max")
                    for (i in 1..(c[Calendar.YEAR] - years[0])) {
                        val year = years[0] + i
                        if (hasYearHistory(year)) {
                            years.add(year)
                        }
                    }
                }
            }
            years
        }

    private fun hasYearHistory(year: Int): Boolean {
        val calendar = Calendar.getInstance()
        calendar.set(year,0, 1, 0,0, 0)
        val min = calendar.timeInMillis
        calendar.set(year, 11, 31, 23, 59, 59)
        return this.database.query(
            StringBuilder("SELECT COUNT(d.id) c FROM day d ")
                .append("INNER JOIN meal m ON d.id = m.day_id ")
                .append("INNER JOIN food_meal fm ON m.id = fm.meal_id ")
                .append("WHERE date >= $min AND date <= ${calendar.timeInMillis}")
                .toString()
        ).use { rs ->
            rs.next().long("c") > 0
        }
    }

    override fun months(year: Int): List<Calendar> =
        this.database.query(monthsQuery(year)).use { rs ->
            val months = TreeMap<Int, Calendar>()
            var r = rs.next()
            if (r.has("date")) {
                do {
                    val calendar = Calendar.getInstance()
                    calendar.timeInMillis = r.long("date")
                    if (!months.containsKey(calendar[Calendar.MONTH])) {
                        months[calendar[Calendar.MONTH]] = calendar
                    }
                    if (!rs.hasNext()) {
                        break
                    }
                    r = rs.next()
                } while (true)
            }
            ArrayList(months.values)
        }

    private fun monthsQuery(year: Int): String {
        val calendar = Calendar.getInstance()
        calendar.set(year,0, 1, 0,0, 0)
        val min = calendar.timeInMillis
        calendar.set(year, 11, 31, 23, 59, 59)
        return StringBuilder("SELECT date FROM day d ")
            .append("INNER JOIN meal m ON d.id = m.day_id ")
            .append("INNER JOIN food_meal fm ON m.id = fm.meal_id ")
            .append("WHERE date >= $min AND date <= ${calendar.timeInMillis} ")
            .append("ORDER by date ASC")
            .toString()
    }
}