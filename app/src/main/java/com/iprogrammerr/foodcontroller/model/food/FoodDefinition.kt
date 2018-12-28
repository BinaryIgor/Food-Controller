package com.iprogrammerr.foodcontroller.model.food

interface FoodDefinition {

    fun id(): Long

    fun name(): String

    fun calories(): Int

    fun protein(): Double

    fun update(values: Map<String, Any>)
}