package com.iprogrammerr.foodcontroller.model.food

interface Portions {

    fun exists(definitionId: Long, weight: Int): Boolean

    fun create(definitionId: Long, weight: Int)

    fun add(definitionId: Long, weight: Int, mealId: Long)

    fun update(foodId: Long, mealId: Long, definitionId: Long, weight: Int)
}