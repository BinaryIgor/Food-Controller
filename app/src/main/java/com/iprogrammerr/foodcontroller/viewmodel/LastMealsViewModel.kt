package com.iprogrammerr.foodcontroller.viewmodel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.iprogrammerr.foodcontroller.ObjectsPool
import com.iprogrammerr.foodcontroller.model.Asynchronous
import com.iprogrammerr.foodcontroller.model.meal.Meal
import com.iprogrammerr.foodcontroller.model.meal.Meals
import com.iprogrammerr.foodcontroller.model.result.Callback

class LastMealsViewModel(
    private val asynchronous: Asynchronous,
    private val id: Long,
    private val meals: Meals,
    limit: Int
) : ViewModel() {

    private val last by lazy {
        this.meals.last(limit)
    }

    companion object {
        fun factory(id: Long, limit: Int) = object : ViewModelProvider.NewInstanceFactory() {

            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(clazz: Class<T>): T = when {
                clazz.isAssignableFrom(LastMealsViewModel::class.java) ->
                    LastMealsViewModel(
                        ObjectsPool.single(Asynchronous::class.java),
                        id,
                        ObjectsPool.single(Meals::class.java),
                        limit
                    ) as T
                else -> throw Exception(
                    "$clazz is not a ${LastMealsViewModel::class.java.simpleName}"
                )
            }
        }
    }

    fun last(callback: Callback<List<Meal>>) {
        this.asynchronous.execute({ this.last }, callback)
    }

    fun clone(meal: Meal, callback: Callback<Boolean>) {
        this.asynchronous.execute({
            this.meals.clone(this.id, meal)
            true
        }, callback)
    }
}