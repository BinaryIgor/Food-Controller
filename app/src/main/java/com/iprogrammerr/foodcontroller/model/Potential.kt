package com.iprogrammerr.foodcontroller.model

class Potential<T>(var value: T?) {

    constructor() : this(null)

    fun value(): T {
        if (this.value == null) {
            throw Exception("Potential does not have any value")
        }
        return this.value as T
    }

    fun hasValue() = this.value != null

    fun isEmpty() = this.value == null

    fun revalue(value: T) {
        this.value = value
    }

    fun empty() {
        this.value = null
    }
}