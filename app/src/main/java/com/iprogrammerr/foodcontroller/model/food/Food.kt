package com.iprogrammerr.foodcontroller.model.food

interface Food {

    fun id(): Int

    fun name(): String

    fun weight(): Int

    fun updateWeight(weight: Int)

    fun calories(): Int

    fun protein(): Int
}