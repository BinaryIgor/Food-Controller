package com.iprogrammerr.foodcontroller.model.day

import com.iprogrammerr.foodcontroller.model.NutritionalValues

interface Days {

    fun range(from: String, to: String): List<Day>

    fun day(date: String): Day

    fun exists(date: String): Boolean

    fun create(goals: NutritionalValues)
}