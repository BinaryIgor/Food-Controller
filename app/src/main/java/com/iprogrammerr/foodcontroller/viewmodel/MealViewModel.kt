package com.iprogrammerr.foodcontroller.viewmodel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.iprogrammerr.foodcontroller.model.Asynchronous
import com.iprogrammerr.foodcontroller.model.meal.Meal
import com.iprogrammerr.foodcontroller.model.meal.Meals
import com.iprogrammerr.foodcontroller.model.result.Callback
import com.iprogrammerr.foodcontroller.model.scalar.StickyScalar
import com.iprogrammerr.foodcontroller.pool.ObjectsPool

class MealViewModel(
    private val asynchronous: Asynchronous,
    private var id: Long,
    private val meals: Meals
) : ViewModel() {

    constructor(asynchronous: Asynchronous, meals: Meals) : this(
        asynchronous, -1, meals
    )

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
                            "$clazz is not a ${MealViewModel::class.java.simpleName}")
                    }
            }
    }

    private val meal = StickyScalar { this.meals.meal(this.id) }

    fun meal(callback: Callback<Meal>) {
        this.asynchronous.execute({ this.meal.value() }, callback)
    }

    fun create(time: Long, dayId: Long, callback: Callback<Long>) {
        this.asynchronous.execute({
            this.id = this.meals.create(time, dayId)
            this.meal.unstick()
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

    override fun onCleared() {
        super.onCleared()
        this.asynchronous.execute({
            if (this.id > 0 && this.meal.value().food().isEmpty()) {
                this.meals.delete(this.id)
            }
            true
        }, Callback.Empty())
    }
}