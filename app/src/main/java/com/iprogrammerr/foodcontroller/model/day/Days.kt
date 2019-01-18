package com.iprogrammerr.foodcontroller.model.day

interface Days {

    fun range(from: Long, to: Long): List<Day>

    fun day(date: Long): Day

    fun exists(date: Long): Boolean

    fun create(weight: Double, caloriesGoal: Int, proteinGoal: Int)

    fun delete(id: Long)
}