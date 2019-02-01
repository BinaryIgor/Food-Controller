package com.iprogrammerr.foodcontroller.viewmodel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.iprogrammerr.foodcontroller.ObjectsPool
import com.iprogrammerr.foodcontroller.model.Asynchronous
import com.iprogrammerr.foodcontroller.model.NutritionalValues
import com.iprogrammerr.foodcontroller.model.day.Days
import com.iprogrammerr.foodcontroller.model.result.Callback

class DayGoalsViewModel(
    private val asynchronous: Asynchronous,
    days: Days,
    date: Long
) : ViewModel() {

    private val day by lazy {
        days.day(date)
    }

    companion object {
        fun factory(date: Long) = object : ViewModelProvider.NewInstanceFactory() {

            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(clazz: Class<T>): T = when {
                clazz.isAssignableFrom(DayGoalsViewModel::class.java) ->
                    DayGoalsViewModel(
                        ObjectsPool.single(Asynchronous::class.java),
                        ObjectsPool.single(Days::class.java),
                        date
                    ) as T
                else -> throw Exception(
                    "$clazz is not a ${DayGoalsViewModel::class.java.simpleName}"
                )
            }
        }
    }

    fun goals(callback: Callback<NutritionalValues>) {
        this.asynchronous.execute({ this.day.goals() }, callback)
    }

    fun save(calories: Int, protein: Int, callback: Callback<Boolean>) {
        this.asynchronous.execute({
            this.day.changeGoals(calories, protein)
            true
        }, callback)
    }
}