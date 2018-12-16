package com.iprogrammerr.foodcontroller.model.category

interface Categories {

    fun all(): List<Category>

    fun create(name: String)
}