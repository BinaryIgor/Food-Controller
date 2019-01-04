package com.iprogrammerr.foodcontroller.model

import android.os.Handler
import android.os.Looper
import com.iprogrammerr.foodcontroller.model.result.Callback
import com.iprogrammerr.foodcontroller.model.result.ResultValue
import java.util.concurrent.Executor

class ReturningAsynchronous(private val executor: Executor, private val returning: Handler) : Asynchronous {

    constructor(executor: Executor) : this(executor, Handler(Looper.getMainLooper()))

    override fun <T> execute(function: () -> T, callback: Callback<T>) {
        this.executor.execute {
            val result = try {
                ResultValue(function())
            } catch (e: Exception) {
                ResultValue<T>(e)
            }
            this.returning.post { callback.call(result) }
        }
    }
}