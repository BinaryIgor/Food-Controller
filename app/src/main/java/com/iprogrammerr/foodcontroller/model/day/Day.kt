package com.iprogrammerr.foodcontroller.model.day

import com.iprogrammerr.foodcontroller.model.NutritionalValues
import com.iprogrammerr.foodcontroller.model.meal.Meal

interface Day {

    fun date(): Long

    fun weight(): Double

    fun goals(): NutritionalValues

    fun meals(): List<Meal>

    fun addMeal(id: Long)

    fun removeMeal(id: Long)
}