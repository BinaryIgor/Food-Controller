package com.iprogrammerr.foodcontroller.model.food

import com.iprogrammerr.foodcontroller.model.Named

interface FoodDefinition : Named {

    fun id(): Long

    fun calories(): Int

    fun protein(): Double

    fun update(name: String, calories: Int, protein: Double)
}