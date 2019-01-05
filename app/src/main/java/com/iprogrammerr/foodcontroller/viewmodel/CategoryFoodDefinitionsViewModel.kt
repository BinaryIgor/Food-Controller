package com.iprogrammerr.foodcontroller.viewmodel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.iprogrammerr.foodcontroller.model.Asynchronous
import com.iprogrammerr.foodcontroller.model.category.Category
import com.iprogrammerr.foodcontroller.model.food.FoodDefinition
import com.iprogrammerr.foodcontroller.model.result.Callback
import com.iprogrammerr.foodcontroller.model.scalar.StartsWithFilter
import com.iprogrammerr.foodcontroller.model.scalar.StickyScalar
import com.iprogrammerr.foodcontroller.pool.ObjectsPool

class CategoryFoodDefinitionsViewModel(private val asynchronous: Asynchronous, private val category: Category) :
    ViewModel() {

    private val food = StickyScalar { this.category.food() }
    private val name = StickyScalar { this.category.name() }

    companion object {
        fun factory(category: Category) = object : ViewModelProvider.NewInstanceFactory() {

            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(clazz: Class<T>): T = when {
                clazz.isAssignableFrom(CategoryFoodDefinitionsViewModel::class.java) ->
                    CategoryFoodDefinitionsViewModel(
                        ObjectsPool.single(Asynchronous::class.java),
                        category
                    ) as T
                else -> throw Exception("$clazz is not a ${CategoryFoodDefinitionsViewModel::class.java.simpleName}")
            }

        }
    }

    fun filtered(criteria: String, callback: Callback<List<FoodDefinition>>) {
        this.asynchronous.execute({ StartsWithFilter(this.food.value(), criteria).value() }, callback)
    }

    fun all(callback: Callback<List<FoodDefinition>>) {
        this.asynchronous.execute({ this.category.food() }, callback)
    }

    fun name(callback: Callback<String>) {
        this.asynchronous.execute({ this.name.value() }, callback)
    }

    fun refresh() {
        this.food.unstick()
    }
}