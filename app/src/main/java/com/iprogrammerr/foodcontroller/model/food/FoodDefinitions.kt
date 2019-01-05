package com.iprogrammerr.foodcontroller.model.food

interface FoodDefinitions {

    fun create(name: String, calories: Int, protein: Double, categoryId: Long)

    fun all(): List<FoodDefinition>

    fun definition(id: Long): FoodDefinition

    fun delete(id: Long)
}