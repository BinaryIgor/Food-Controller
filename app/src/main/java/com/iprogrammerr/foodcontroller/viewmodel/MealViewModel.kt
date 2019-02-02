package com.iprogrammerr.foodcontroller.viewmodel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.iprogrammerr.foodcontroller.ObjectsPool
import com.iprogrammerr.foodcontroller.model.Asynchronous
import com.iprogrammerr.foodcontroller.model.meal.Meal
import com.iprogrammerr.foodcontroller.model.meal.Meals
import com.iprogrammerr.foodcontroller.model.result.Callback
import com.iprogrammerr.foodcontroller.model.scalar.StickyScalar

class MealViewModel(
    private val asynchronous: Asynchronous,
    private var id: Long,
    private val meals: Meals
) : ViewModel() {

    private val meal = StickyScalar { this.meals.meal(this.id) }

    constructor(id: Long) : this(
        ObjectsPool.single(Asynchronous::class.java),
        id,
        ObjectsPool.single(Meals::class.java)
    )

    constructor() : this(-1)

    companion object {
        fun factory(id: Long) =
            object : ViewModelProvider.NewInstanceFactory() {

                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel?> create(clazz: Class<T>): T =
                    when {
                        clazz.isAssignableFrom(MealViewModel::class.java) -> MealViewModel(
                            id
                        ) as T
                        else -> throw Exception(
                            "$clazz is not a ${MealViewModel::class.java.simpleName}"
                        )
                    }
            }
    }

    fun meal(callback: Callback<Meal>) {
        this.asynchronous.execute({
            this.meal.value()
        }, callback)
    }

    fun create(time: Long, dayId: Long, callback: Callback<Long>) {
        this.asynchronous.execute({
            this.id = this.meals.create(time, dayId)
            this.meal.unstick()
            println("New meal id = ${this.id}")
            this.id
        }, callback)
    }

    fun changeTime(time: Long, callback: Callback<Boolean>) {
        this.asynchronous.execute({
            this.meal.value().changeTime(time)
            true
        }, callback)
    }

    fun removeFood(id: Long, callback: Callback<Boolean>) {
        this.asynchronous.execute({
            this.meal.value().removeFood(id)
            true
        }, callback)
    }

    fun refresh() {
        this.meal.unstick()
    }
}