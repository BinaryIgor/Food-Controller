package com.iprogrammerr.foodcontroller.viewmodel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.iprogrammerr.foodcontroller.ObjectsPool
import com.iprogrammerr.foodcontroller.model.Asynchronous
import com.iprogrammerr.foodcontroller.model.NutritionalValues
import com.iprogrammerr.foodcontroller.model.day.Day
import com.iprogrammerr.foodcontroller.model.day.Days
import com.iprogrammerr.foodcontroller.model.result.Callback
import com.iprogrammerr.foodcontroller.model.scalar.StickyScalar
import java.util.*

class DaysViewModel(
    private val asynchronous: Asynchronous,
    days: Days,
    month: Calendar
) : ViewModel() {

    private val days = StickyScalar {
        month.set(Calendar.DAY_OF_MONTH, 1)
        val start = month.timeInMillis
        month.set(Calendar.DAY_OF_MONTH, month.getActualMaximum(Calendar.DAY_OF_MONTH))
        days.range(start, month.timeInMillis)
    }

    companion object {
        fun factory(month: Calendar) = object : ViewModelProvider.NewInstanceFactory() {

            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(clazz: Class<T>): T = when {
                clazz.isAssignableFrom(DaysViewModel::class.java) ->
                    DaysViewModel(
                        ObjectsPool.single(Asynchronous::class.java),
                        ObjectsPool.single(Days::class.java),
                        month
                    ) as T
                else -> throw Exception(
                    "$clazz is not a ${DaysViewModel::class.java.simpleName}"
                )
            }
        }
    }

    fun days(callback: Callback<List<Day>>) {
        this.asynchronous.execute({ this.days.value() }, callback)
    }

    fun statistics(callback: Callback<Pair<NutritionalValues, NutritionalValues>>) {
        this.asynchronous.execute({
            var calories = 0
            var protein = 0.0
            var caloriesGoal = 0
            var proteinGoal = 0.0
            if (this.days.value().isNotEmpty()) {
                for (d in this.days.value()) {
                    for (m in d.meals()) {
                        val values = m.nutritionalValues()
                        calories += values.calories()
                        protein += values.protein()
                    }
                    val goals = d.goals()
                    caloriesGoal += goals.calories()
                    proteinGoal += goals.protein()
                }
                calories /= this.days.value().size
                protein /= this.days.value().size
                caloriesGoal /= this.days.value().size
                proteinGoal /= this.days.value().size
            }
            Pair(
                object : NutritionalValues {

                    override fun calories() = calories

                    override fun protein() = protein
                },
                object : NutritionalValues {

                    override fun calories() = caloriesGoal

                    override fun protein() = proteinGoal
                }
            )
        }, callback)
    }

    fun refresh() {
        this.days.unstick()
    }
}