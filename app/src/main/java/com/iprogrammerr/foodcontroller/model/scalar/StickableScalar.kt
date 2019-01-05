package com.iprogrammerr.foodcontroller.model.scalar

interface StickableScalar<T> : Scalar<T> {
    fun unstick()
}