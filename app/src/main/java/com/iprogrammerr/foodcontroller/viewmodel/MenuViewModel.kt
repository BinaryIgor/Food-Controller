package com.iprogrammerr.foodcontroller.viewmodel

import android.arch.lifecycle.ViewModel
import com.iprogrammerr.foodcontroller.model.NutritionalValues
import com.iprogrammerr.foodcontroller.model.StickableScalar
import com.iprogrammerr.foodcontroller.model.StickyScalar
import com.iprogrammerr.foodcontroller.model.day.Days
import com.iprogrammerr.foodcontroller.model.day.Weight
import com.iprogrammerr.foodcontroller.model.goals.Goals
import com.iprogrammerr.foodcontroller.model.result.Result
import com.iprogrammerr.foodcontroller.model.result.ResultValue
import com.iprogrammerr.foodcontroller.pool.ObjectsPool
import java.util.concurrent.Executor

class MenuViewModel(
    private val executor: Executor,
    private val days: Days,
    private val weight: Weight,
    private val goals: Goals
) : ViewModel() {

    private val started: StickableScalar<Result<Boolean>>
    private val lastWeight: StickableScalar<Result<Double>>

    constructor() : this(
        ObjectsPool.single(Executor::class.java), ObjectsPool.single(Days::class.java),
        ObjectsPool.single(Weight::class.java), ObjectsPool.single(Goals::class.java)
    )

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

    fun dayStarted(callback: (Result<Boolean>) -> Unit) {
        this.executor.execute {
            if (!this.started.value().isSuccess()) {
                this.started.unstick()
            }
            callback(this.started.value())
        }
    }

    fun lastWeight(callback: (Result<Double>) -> Unit) {
        this.executor.execute {
            if (!this.lastWeight.value().isSuccess()) {
                this.lastWeight.unstick()
            }
            callback(this.lastWeight.value())
        }
    }

    fun createDay(weight: Double, callback: (Result<Boolean>) -> Unit) {
        this.executor.execute {
            try {
                this.days.create(weight, object : NutritionalValues {

                    override fun calories() = this@MenuViewModel.goals.calories().value()

                    override fun protein() = this@MenuViewModel.goals.protein().value()
                })
                callback(ResultValue(true))
            } catch (e: Exception) {
                e.printStackTrace()
                callback(ResultValue(e))
            }
        }
    }

    fun refresh() {
        this.started.unstick()
        this.lastWeight.unstick()
    }
}