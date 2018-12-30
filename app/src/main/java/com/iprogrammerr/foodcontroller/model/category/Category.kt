package com.iprogrammerr.foodcontroller.model.category

import com.iprogrammerr.foodcontroller.model.food.FoodDefinition

interface Category {

    fun id(): Long

    fun name(): String

    fun rename(name: String)

    fun food(): List<FoodDefinition>

    fun addFood(id: Long)

    class Fake(private val name: String) : Category {

        override fun id() = -1L

        override fun name() = this.name

        override fun rename(name: String) {

        }

        override fun food() = ArrayList<FoodDefinition>()

        override fun addFood(id: Long) {

        }
    }
}