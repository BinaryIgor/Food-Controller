package com.iprogrammerr.foodcontroller.viewmodel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.iprogrammerr.foodcontroller.ObjectsPool
import com.iprogrammerr.foodcontroller.model.Asynchronous
import com.iprogrammerr.foodcontroller.model.day.Days
import com.iprogrammerr.foodcontroller.model.day.DaysWithStatistics
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
        DaysWithStatistics(days.range(start, month.timeInMillis))
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

    fun days(callback: Callback<DaysWithStatistics>) {
        this.asynchronous.execute({ this.days.value() }, callback)
    }

    fun refresh() {
        this.days.unstick()
    }
}