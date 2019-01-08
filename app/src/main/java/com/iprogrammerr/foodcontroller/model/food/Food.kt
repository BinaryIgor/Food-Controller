package com.iprogrammerr.foodcontroller.model.food

interface Food {

    fun id(): Long

    fun name(): String

    fun weight(): Int

    fun calories(): Int

    fun protein(): Double
}