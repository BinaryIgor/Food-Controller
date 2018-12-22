package com.iprogrammerr.foodcontroller.model.result

interface Result<T> {

    fun value(): T

    fun isSuccess(): Boolean

    fun exception(): String
}