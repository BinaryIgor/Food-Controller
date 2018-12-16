package com.iprogrammerr.foodcontroller.model

class StickyScalar<T>(private val source: () -> T) : Scalar<T> {

    private var value: T? = null

    override fun value(): T {
        if (this.value == null) {
            this.value = this.source()
        }
        return this.value as T
    }
}