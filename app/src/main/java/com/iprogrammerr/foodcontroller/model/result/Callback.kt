package com.iprogrammerr.foodcontroller.model.result

interface Callback<T> {
    fun call(result: Result<T>)

    class Empty<T> : Callback<T> {

        override fun call(result: Result<T>) {

        }
    }
}