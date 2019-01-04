package com.iprogrammerr.foodcontroller.model

import com.iprogrammerr.foodcontroller.model.result.Callback

interface Asynchronous {
    fun <T> execute(function: () -> T, callback: Callback<T>)
}