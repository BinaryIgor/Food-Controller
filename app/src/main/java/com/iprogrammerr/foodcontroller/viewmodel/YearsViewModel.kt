package com.iprogrammerr.foodcontroller.viewmodel

import android.arch.lifecycle.ViewModel
import com.iprogrammerr.foodcontroller.ObjectsPool
import com.iprogrammerr.foodcontroller.model.Asynchronous
import com.iprogrammerr.foodcontroller.model.history.History
import com.iprogrammerr.foodcontroller.model.result.Callback

class YearsViewModel(
    private val asynchronous: Asynchronous,
    history: History
) : ViewModel() {

    private val years by lazy {
        history.years()
    }

    constructor() : this(
        ObjectsPool.single(Asynchronous::class.java),
        ObjectsPool.single(History::class.java)
    )

    fun years(callback: Callback<List<Int>>) {
        this.asynchronous.execute({ this.years }, callback)
    }
}