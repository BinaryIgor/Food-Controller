package com.iprogrammerr.foodcontroller.model.scalar

import java.util.*

class HourMinutes(private val time: Long) : Scalar<String> {

    override fun value(): String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = this.time
        return "${calendar[Calendar.HOUR_OF_DAY]}:${calendar[Calendar.MINUTE]}"
    }
}