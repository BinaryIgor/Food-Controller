package com.iprogrammerr.foodcontroller.model

class StickyScalar<T>(private val source: () -> T) : StickableScalar<T> {

    private var value: T? = null
    private var sticky = true

    override fun value(): T {
        if (this.value == null || !this.sticky) {
            this.value = this.source()
            this.sticky = true
        }
        return this.value as T
    }

    override fun unstick() {
        this.sticky = false
    }
}