package com.iprogrammerr.foodcontroller.model

interface StickableScalar<T> : Scalar<T> {
    fun unstick()
}