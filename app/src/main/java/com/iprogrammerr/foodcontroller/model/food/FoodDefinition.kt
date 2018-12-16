package com.iprogrammerr.foodcontroller.model.food

interface FoodDefinition {

    fun name(): String

    fun calories(): Int

    fun protein(): Int

    fun update(values: Map<String, Any>)
}