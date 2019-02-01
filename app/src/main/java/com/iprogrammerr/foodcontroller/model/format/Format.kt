package com.iprogrammerr.foodcontroller.model.format

interface Format<T> {
    fun formatted(value: T): String
}