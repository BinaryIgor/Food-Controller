package com.iprogrammerr.foodcontroller.viewmodel

import android.arch.lifecycle.ViewModel
import com.iprogrammerr.foodcontroller.model.NutritionalValues
import com.iprogrammerr.foodcontroller.model.StickableScalar
import com.iprogrammerr.foodcontroller.model.StickyScalar
import com.iprogrammerr.foodcontroller.model.day.Day
import com.iprogrammerr.foodcontroller.model.day.Days
import com.iprogrammerr.foodcontroller.model.result.Result
import com.iprogrammerr.foodcontroller.model.result.ResultValue
import com.iprogrammerr.foodcontroller.pool.ObjectsPool
import java.util.concurrent.Executor

class DayViewModel(private val executor: Executor, date: Long, private val days: Days) : ViewModel() {

    constructor(date: Long) : this(ObjectsPool.single(Executor::class.java), date, ObjectsPool.single(Days::class.java))

    constructor() : this(System.currentTimeMillis())

    private val day: StickableScalar<Result<Day>>

    init {
        this.day = StickyScalar {
            try {
                ResultValue(this.days.day(date))
            } catch (e: Exception) {
                e.printStackTrace()
                ResultValue<Day>(e.message as String)
            }
        }
    }

    fun day(callback: (Result<Day>) -> Unit) {
        this.executor.execute { callback(this.day.value()) }
    }

    fun changeWeight(weight: Double, callback: (Result<Boolean>) -> Unit) {
        this.executor.execute {
            try {
                this.day.value().value().changeWeight(weight)
                callback(ResultValue(true))
            } catch (e: Exception) {
                callback(ResultValue(e))
            }
        }
    }
}