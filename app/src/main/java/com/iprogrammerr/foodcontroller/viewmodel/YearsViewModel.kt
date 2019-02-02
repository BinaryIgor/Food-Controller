package com.iprogrammerr.foodcontroller.viewmodel

import android.arch.lifecycle.ViewModel
import com.iprogrammerr.foodcontroller.ObjectsPool
import com.iprogrammerr.foodcontroller.model.Asynchronous
import com.iprogrammerr.foodcontroller.model.history.History
import com.iprogrammerr.foodcontroller.model.result.Callback
import com.iprogrammerr.foodcontroller.model.scalar.StickyScalar

class YearsViewModel(
    private val asynchronous: Asynchronous,
    history: History
) : ViewModel() {

    private val years = StickyScalar { history.years() }

    constructor() : this(
        ObjectsPool.single(Asynchronous::class.java),
        ObjectsPool.single(History::class.java)
    )

    fun years(callback: Callback<List<Int>>) {
        this.asynchronous.execute({ this.years.value() }, callback)
    }

    fun refresh() {
        this.years.unstick()
    }
}