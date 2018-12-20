package com.iprogrammerr.foodcontroller.model.meal

import com.iprogrammerr.foodcontroller.model.NutritionalValues
import com.iprogrammerr.foodcontroller.model.food.Food

interface Meal {

    fun id(): Long

    fun time(): Long

    fun food(): List<Food>

    fun nutritionalValues(): NutritionalValues

    fun removeFood(id: Long)
}