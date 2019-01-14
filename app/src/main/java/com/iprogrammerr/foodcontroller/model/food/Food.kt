package com.iprogrammerr.foodcontroller.model.food

import com.iprogrammerr.foodcontroller.model.NutritionalValues

interface Food {

    fun id(): Long

    fun name(): String

    fun weight(): Int

    fun values(): NutritionalValues

    fun definition(): FoodDefinition
}