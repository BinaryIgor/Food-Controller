package com.iprogrammerr.foodcontroller.view.message

interface Message {
    fun value(): String


    object FoodDefinitionsChanged : Message {

        override fun value() = "FoodDefinitionsChanged"
    }
}