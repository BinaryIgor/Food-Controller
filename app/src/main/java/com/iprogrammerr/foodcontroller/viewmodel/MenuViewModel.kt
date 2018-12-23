package com.iprogrammerr.foodcontroller.viewmodel

import android.arch.lifecycle.ViewModel
import com.iprogrammerr.foodcontroller.model.StickableScalar
import com.iprogrammerr.foodcontroller.model.StickyScalar
import com.iprogrammerr.foodcontroller.model.day.Days
import com.iprogrammerr.foodcontroller.model.day.Weight
import com.iprogrammerr.foodcontroller.model.result.Result
import com.iprogrammerr.foodcontroller.model.result.ResultValue
import com.iprogrammerr.foodcontroller.pool.ObjectsPool
import java.util.concurrent.Executor

class MenuViewModel private constructor(
    private val executor: Executor,
    private val days: Days,
    private val weight: Weight
) : ViewModel() {

    constructor() : this (
        ObjectsPool.single(Executor::class.java), ObjectsPool.single(Days::class.java),
        ObjectsPool.single(Weight::class.java)
    )

    private val started: StickableScalar<Result<Boolean>>
    private val lastWeight: StickableScalar<Result<Double>>

    init {
        this.started = StickyScalar {
            try {
                ResultValue(this.days.exists(System.currentTimeMillis()))
            } catch (e: Exception) {
                ResultValue<Boolean>(e.message as String)
            }
        }
        this.lastWeight = StickyScalar {
            try {
                ResultValue(this.weight.value())
            } catch (e: Exception) {
                ResultValue<Double>(e.message as String)
            }
        }
    }

    fun dayStarted(result: (Result<Boolean>) -> Unit) {
        this.executor.execute {
            if (!this.started.value().isSuccess()) {
                this.started.unstick()
            }
            result(this.started.value())
        }
    }

    fun lastWeight(result: (Result<Double>) -> Unit) {
        this.executor.execute {
            if (!this.lastWeight.value().isSuccess()) {
                this.lastWeight.unstick()
            }
            result(this.lastWeight.value())
        }
    }
}