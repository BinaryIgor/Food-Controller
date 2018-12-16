package com.iprogrammerr.foodcontroller.model.food

interface FoodDefinitions {

    fun definition(id: Long): FoodDefinition

    fun delete(id: Long)
}