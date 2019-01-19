package com.iprogrammerr.foodcontroller.model.day

import com.iprogrammerr.foodcontroller.model.NutritionalValues
import com.iprogrammerr.foodcontroller.model.meal.Meal

interface Day {

    fun id(): Long

    fun date(): Long

    fun weight(): Double

    fun changeWeight(weight: Double)

    fun changeGoals(calories: Int, protein: Int)

    fun goals(): NutritionalValues

    fun meals(): List<Meal>
}