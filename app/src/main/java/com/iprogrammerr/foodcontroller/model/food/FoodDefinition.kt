package com.iprogrammerr.foodcontroller.model.food

interface FoodDefinition {

    fun id(): Long

    fun name(): String

    fun calories(): Int

    fun protein(): Double

    fun update(name: String, calories: Int, protein: Double)
}