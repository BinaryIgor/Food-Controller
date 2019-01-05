package com.iprogrammerr.foodcontroller.viewmodel

import android.arch.lifecycle.ViewModel
import com.iprogrammerr.foodcontroller.model.Asynchronous
import com.iprogrammerr.foodcontroller.model.scalar.StickyScalar
import com.iprogrammerr.foodcontroller.model.day.Day
import com.iprogrammerr.foodcontroller.model.day.Days
import com.iprogrammerr.foodcontroller.model.result.Callback
import com.iprogrammerr.foodcontroller.pool.ObjectsPool

class DayViewModel(private val asynchronous: Asynchronous, date: Long, private val days: Days) : ViewModel() {

    constructor(date: Long) : this(
        ObjectsPool.single(Asynchronous::class.java),
        date,
        ObjectsPool.single(Days::class.java)
    )

    constructor() : this(System.currentTimeMillis())

    private val day = StickyScalar { this.days.day(date) }

    fun day(callback: Callback<Day>) {
        this.asynchronous.execute({
            this.day.value()
        }, callback)
    }

    fun changeWeight(weight: Double, callback: Callback<Boolean>) {
        this.asynchronous.execute({
            this.day.value().changeWeight(weight)
            true
        }, callback)
    }
}