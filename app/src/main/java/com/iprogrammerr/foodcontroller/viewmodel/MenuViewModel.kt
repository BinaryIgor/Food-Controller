package com.iprogrammerr.foodcontroller.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.iprogrammerr.foodcontroller.livedata.ResultLiveData
import com.iprogrammerr.foodcontroller.model.day.Days
import com.iprogrammerr.foodcontroller.model.result.Result
import com.iprogrammerr.foodcontroller.model.result.ResultValue
import com.iprogrammerr.foodcontroller.pool.ObjectsPool
import java.util.concurrent.Executor

class MenuViewModel private constructor(
    private val executor: Executor,
    private val days: Days,
    private val started: ResultLiveData<Boolean>
) : ViewModel() {

    constructor(executor: Executor, days: Days) : this(executor, days, ResultLiveData())

    constructor() : this(ObjectsPool.single(Executor::class.java), ObjectsPool.single(Days::class.java))

    fun dayStarted(): LiveData<Result<Boolean>> {
        if (this.started.isEmptyOrFailure()) {
            load()
        }
        return this.started
    }

    private fun load() {
        this.executor.execute {
            var result: Result<Boolean>
            try {
                result = ResultValue(this.days.exists(System.currentTimeMillis()))
            } catch (e: Exception) {
                result = ResultValue(e.message as String)
            }
            this.started.postValue(result)
        }
    }
}