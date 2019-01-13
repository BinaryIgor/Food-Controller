package com.iprogrammerr.foodcontroller.viewmodel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.iprogrammerr.foodcontroller.ObjectsPool
import com.iprogrammerr.foodcontroller.model.Asynchronous
import com.iprogrammerr.foodcontroller.model.day.Day
import com.iprogrammerr.foodcontroller.model.day.Days
import com.iprogrammerr.foodcontroller.model.meal.Meals
import com.iprogrammerr.foodcontroller.model.result.Callback
import com.iprogrammerr.foodcontroller.model.scalar.StickyScalar

class DayViewModel(
    private val asynchronous: Asynchronous,
    date: Long,
    private val days: Days,
    private val meals: Meals
) : ViewModel() {

    companion object {
        fun factory(date: Long) = object : ViewModelProvider.NewInstanceFactory() {

            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(clazz: Class<T>): T = when {
                clazz.isAssignableFrom(DayViewModel::class.java) ->
                    DayViewModel(
                        ObjectsPool.single(Asynchronous::class.java),
                        date,
                        ObjectsPool.single(Days::class.java),
                        ObjectsPool.single(Meals::class.java)
                    ) as T
                else -> throw Exception(
                    "$clazz is not a ${DayViewModel::class.java.simpleName}")
            }
        }
    }

    private val day = StickyScalar { this.days.day(date) }

    fun day(callback: Callback<Day>) {
        this.asynchronous.execute({
            this.day.value()
        }, callback)
    }

    fun changeWeight(weight: Double, callback: Callback<Boolean>) {
        this.asynchronous.execute({
            this.day.value().changeWeight(weight)
            true
        }, callback)
    }

    fun deleteMeal(id: Long, callback: Callback<Boolean>) {
        this.asynchronous.execute({
            this.meals.delete(id)
            this.day.unstick()
            true
        }, callback)
    }
}