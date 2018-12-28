package com.iprogrammerr.foodcontroller.model.category

import com.iprogrammerr.foodcontroller.model.food.FoodDefinition

interface Category {

    fun id(): Long

    fun name(): String

    fun rename(name: String)

    fun food(): List<FoodDefinition>
}