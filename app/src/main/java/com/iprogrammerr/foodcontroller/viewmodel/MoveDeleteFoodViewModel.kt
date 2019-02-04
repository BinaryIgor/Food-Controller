package com.iprogrammerr.foodcontroller.viewmodel

import android.arch.lifecycle.ViewModel
import com.iprogrammerr.foodcontroller.model.Asynchronous
import com.iprogrammerr.foodcontroller.model.scalar.StickyScalar
import com.iprogrammerr.foodcontroller.model.category.Categories
import com.iprogrammerr.foodcontroller.model.category.Category
import com.iprogrammerr.foodcontroller.model.food.FoodDefinitions
import com.iprogrammerr.foodcontroller.model.result.Callback
import com.iprogrammerr.foodcontroller.ObjectsPool

class MoveDeleteFoodViewModel(
    private val asynchronous: Asynchronous,
    private val categories: Categories,
    private val definitions: FoodDefinitions
) : ViewModel() {

    private val allCategories = StickyScalar { this.categories.value() }

    constructor() : this(
        ObjectsPool.single(Asynchronous::class.java),
        ObjectsPool.single(Categories::class.java),
        ObjectsPool.single(FoodDefinitions::class.java)
    )

    fun categories(callback: Callback<List<Category>>) {
        this.asynchronous.execute({ this.allCategories.value() }, callback)
    }

    fun move(categoryId: Long, foodId: Long, callback: Callback<Boolean>) {
        this.asynchronous.execute({
            category(categoryId).addFood(foodId)
            true
        }, callback)
    }

    private fun category(id: Long): Category {
        for (c in this.allCategories.value()) {
            if (c.id() == id) {
                return c
            }
        }
        throw Exception("There is no category of $id id")
    }

    fun delete(id: Long, callback: Callback<Boolean>) {
        this.asynchronous.execute({
            this.definitions.delete(id)
            true
        }, callback)
    }
}