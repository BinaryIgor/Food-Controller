package com.iprogrammerr.foodcontroller.viewmodel

import android.arch.lifecycle.ViewModel
import com.iprogrammerr.foodcontroller.model.StickableScalar
import com.iprogrammerr.foodcontroller.model.StickyScalar
import com.iprogrammerr.foodcontroller.model.category.Category
import com.iprogrammerr.foodcontroller.model.food.FoodDefinition
import com.iprogrammerr.foodcontroller.model.result.Result
import com.iprogrammerr.foodcontroller.model.result.ResultValue
import java.util.concurrent.Executor

class CategoryFoodDefinitionsViewModel(private val executor: Executor, private val category: Category) : ViewModel() {

    private val products: StickableScalar<ResultValue<List<FoodDefinition>>> = StickyScalar {
        try {
            ResultValue(this.category.food())
        } catch (e: Exception) {
            ResultValue<List<FoodDefinition>>(e)
        }
    }

    private val name by lazy {
        try {
            ResultValue { this.category.name() }
        } catch (e: Exception) {
            ResultValue<String>(e)
        }
    }

    fun filtered(criteria: String, callback: (Result<List<FoodDefinition>>) -> Unit) {
        this.executor.execute {
            if (this.products.value().isSuccess()) {
                callback(ResultValue(filtered(criteria)))
            } else {
                callback(this.products.value())
            }
        }
    }

    private fun filtered(criteria: String): List<FoodDefinition> {
        val filtered: MutableList<FoodDefinition> = ArrayList()
        for (fd in this.products.value().value()) {
            if (fd.name().startsWith(criteria, true)) {
                filtered.add(fd)
            }
        }
        return filtered
    }

    fun products(callback: (Result<List<FoodDefinition>>) -> Unit) {
        this.executor.execute { callback(this.products.value()) }
    }

    fun name(callback: (Result<String>) -> Unit) {
        this.executor.execute { callback(this.name) }
    }

    fun refresh() {
        this.products.unstick()
    }
}