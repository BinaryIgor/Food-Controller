package com.iprogrammerr.foodcontroller.viewmodel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.iprogrammerr.foodcontroller.ObjectsPool
import com.iprogrammerr.foodcontroller.model.Asynchronous
import com.iprogrammerr.foodcontroller.model.day.Days
import java.util.*

class DaysViewModel(
    private val asynchronous: Asynchronous,
    private val days: Days,
    month: Calendar
) : ViewModel() {

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
                    "$clazz is not a ${DaysViewModel::class.java.simpleName}")
            }
        }
    }
}