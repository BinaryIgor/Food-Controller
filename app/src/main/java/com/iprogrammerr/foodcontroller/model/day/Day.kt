package com.iprogrammerr.foodcontroller.model.day

import com.iprogrammerr.foodcontroller.model.NutritionalValues
import com.iprogrammerr.foodcontroller.model.meal.Meal

interface Day {

    fun goals(): NutritionalValues

    fun date(): Long

    fun meals(): List<Meal>

    fun removeMeal(id: Long)
}