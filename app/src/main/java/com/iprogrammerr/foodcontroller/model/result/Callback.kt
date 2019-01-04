package com.iprogrammerr.foodcontroller.model.result

interface Callback<T> {
    fun call(result: Result<T>)
}