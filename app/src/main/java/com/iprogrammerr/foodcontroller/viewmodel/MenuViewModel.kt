package com.iprogrammerr.foodcontroller.viewmodel

import android.arch.lifecycle.ViewModel
import com.iprogrammerr.foodcontroller.model.StickableScalar
import com.iprogrammerr.foodcontroller.model.StickyScalar
import com.iprogrammerr.foodcontroller.model.day.Days
import com.iprogrammerr.foodcontroller.model.result.Result
import com.iprogrammerr.foodcontroller.model.result.ResultValue
import com.iprogrammerr.foodcontroller.pool.ObjectsPool
import java.util.concurrent.Executor

class MenuViewModel private constructor(
    private val executor: Executor,
    private val days: Days
) : ViewModel() {

    constructor() : this(ObjectsPool.single(Executor::class.java), ObjectsPool.single(Days::class.java))

    private val started: StickableScalar<Result<Boolean>>

    init {
        this.started = StickyScalar {
            try {
                ResultValue(this.days.exists(System.currentTimeMillis()))
            } catch (e: Exception) {
                ResultValue<Boolean>(e.message as String)
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
}