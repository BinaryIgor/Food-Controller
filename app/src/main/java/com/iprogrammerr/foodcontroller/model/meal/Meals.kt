package com.iprogrammerr.foodcontroller.model.meal

interface Meals {

    fun last(limit: Int): List<Meal>

    fun meal(id: Long): Meal

    fun create(time: Long, dayId: Long): Long

    fun clone(id: Long, meal: Meal)

    fun delete(id: Long)
}