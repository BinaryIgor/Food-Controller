package com.iprogrammerr.foodcontroller.view.message

interface Message {
    fun value(): String

    object FoodDefinitionsChanged : Message {
        override fun value() = "FoodDefinitionsChanged"
    }

    object FoodDefinitionMoved : Message {
        override fun value() = "FoodDefinitionMoved"
    }
}