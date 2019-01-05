package com.iprogrammerr.foodcontroller.model.scalar

import java.util.*

class HourMinutes(private val calendar: Calendar) : Scalar<String> {

    constructor(time: Long) : this(Calendar.getInstance()) {
        this.calendar.timeInMillis = time
    }

    override fun value(): String {
        return String.format("%02d:%02d", this.calendar[Calendar.HOUR_OF_DAY], this.calendar[Calendar.MINUTE])
    }
}