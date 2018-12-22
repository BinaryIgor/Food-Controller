package com.iprogrammerr.foodcontroller.model.result

import com.iprogrammerr.foodcontroller.model.Potential

class ResultValue<T> private constructor(private val value: Potential<T>, private val exception: String) : Result<T> {

    constructor(value: T) : this(Potential(value), "")

    constructor(exception: String) : this(Potential(), exception)

    override fun value(): T {
        if (this.value.isEmpty()) {
            throw Exception("Result have exception = ${this.exception}")
        }
        return this.value.value()
    }

    override fun isSuccess() = this.value.hasValue()

    override fun exception() = this.exception
}