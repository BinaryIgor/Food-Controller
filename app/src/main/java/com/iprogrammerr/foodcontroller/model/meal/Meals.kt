package com.iprogrammerr.foodcontroller.model.meal

interface Meals {

    fun all(): List<Meal>

    fun meal(id: Long): Meal

    fun create(time: Long, dayId: Long): Long
}