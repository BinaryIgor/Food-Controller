package com.iprogrammerr.foodcontroller.viewmodel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.iprogrammerr.foodcontroller.ObjectsPool
import com.iprogrammerr.foodcontroller.model.Asynchronous
import com.iprogrammerr.foodcontroller.model.meal.Meal
import com.iprogrammerr.foodcontroller.model.meal.Meals
import com.iprogrammerr.foodcontroller.model.result.Callback

class MealDetailsViewModel(
    private val asynchronous: Asynchronous,
    meals: Meals,
    id: Long
) : ViewModel() {

    private val meal by lazy {
        meals.meal(id)
    }

    companion object {
        fun factory(id: Long) = object : ViewModelProvider.NewInstanceFactory() {

            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(clazz: Class<T>): T =
                when {
                    clazz.isAssignableFrom(
                        MealDetailsViewModel::class.java) -> MealDetailsViewModel(
                        ObjectsPool.single(Asynchronous::class.java),
                        ObjectsPool.single(Meals::class.java),
                        id
                    ) as T
                    else -> throw Exception(
                        "$clazz is not a ${MealDetailsViewModel::class.java.simpleName}")
                }
        }
    }

    fun meal(callback: Callback<Meal>) {
        this.asynchronous.execute({ this.meal }, callback)
    }
}