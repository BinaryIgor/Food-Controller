package com.iprogrammerr.foodcontroller.viewmodel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.iprogrammerr.foodcontroller.ObjectsPool
import com.iprogrammerr.foodcontroller.model.Asynchronous
import com.iprogrammerr.foodcontroller.model.history.History
import com.iprogrammerr.foodcontroller.model.result.Callback
import com.iprogrammerr.foodcontroller.model.scalar.StickyScalar
import java.util.*

class MonthsViewModel(
    private val asynchronous: Asynchronous, history: History, year: Int
) : ViewModel() {

    private val months = StickyScalar {
        history.months(year)
    }

    companion object {
        fun factory(year: Int) = object : ViewModelProvider.NewInstanceFactory() {

            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(clazz: Class<T>): T = when {
                clazz.isAssignableFrom(MonthsViewModel::class.java) ->
                    MonthsViewModel(
                        ObjectsPool.single(Asynchronous::class.java),
                        ObjectsPool.single(History::class.java),
                        year
                    ) as T
                else -> throw Exception(
                    "$clazz is not a ${MonthsViewModel::class.java.simpleName}")
            }
        }
    }

    fun months(callback: Callback<List<Calendar>>) {
        this.asynchronous.execute({ this.months.value() }, callback)
    }

    fun refresh() {
        this.months.unstick()
    }
}