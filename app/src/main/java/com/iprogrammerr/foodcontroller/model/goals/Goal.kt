package com.iprogrammerr.foodcontroller.model.goals

interface Goal<T> {

    fun value(): T

    fun change(value: T)
}