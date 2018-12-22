package com.iprogrammerr.foodcontroller.model.day

import com.iprogrammerr.foodcontroller.model.NutritionalValues

interface Days {

    fun range(from: Long, to: Long): List<Day>

    fun day(date: Long): Day

    fun exists(date: Long): Boolean

    fun create(goals: NutritionalValues)
}