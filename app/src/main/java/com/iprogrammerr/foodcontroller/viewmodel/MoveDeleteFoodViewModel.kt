package com.iprogrammerr.foodcontroller.viewmodel

import android.arch.lifecycle.ViewModel
import com.iprogrammerr.foodcontroller.model.category.Categories
import com.iprogrammerr.foodcontroller.model.category.Category
import com.iprogrammerr.foodcontroller.model.food.FoodDefinitions
import com.iprogrammerr.foodcontroller.model.result.Result
import com.iprogrammerr.foodcontroller.model.result.ResultValue
import java.util.concurrent.Executor

class MoveDeleteFoodViewModel(
    private val executor: Executor,
    private val categories: Categories,
    private val definitions: FoodDefinitions
) : ViewModel() {

    private val allCategories by lazy {
        try {
            ResultValue(this.categories.all())
        } catch (e: Exception) {
            ResultValue<List<Category>>(e)
        }
    }

    fun categories(callback: (Result<List<Category>>) -> Unit) {
        this.executor.execute { callback(this.allCategories) }
    }

    fun move(categoryId: Long, foodId: Long, callback: (Result<Boolean>) -> Unit) {
        this.executor.execute {
            try {
                category(categoryId).addFood(foodId)
                callback(ResultValue(true))
            } catch (e: Exception) {
                callback(ResultValue(e))
            }
        }
    }

    private fun category(id: Long): Category {
        for (c in this.allCategories.value()) {
            if (c.id() == id) {
                return c
            }
        }
        throw Exception("There is no category of $id id")
    }

    fun delete(id: Long, callback: (Result<Boolean>) -> Unit) {
        this.executor.execute {
            try {
                this.definitions.delete(id)
                callback(ResultValue(true))
            } catch (e: Exception) {
                callback(ResultValue(e))
            }
        }
    }
}