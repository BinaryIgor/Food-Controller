package com.iprogrammerr.foodcontroller.viewmodel

import android.arch.lifecycle.ViewModel
import com.iprogrammerr.foodcontroller.model.Asynchronous
import com.iprogrammerr.foodcontroller.model.food.FoodDefinition
import com.iprogrammerr.foodcontroller.model.food.FoodDefinitions
import com.iprogrammerr.foodcontroller.model.result.Callback
import com.iprogrammerr.foodcontroller.pool.ObjectsPool

class FoodViewModel(private val asynchronous: Asynchronous, private val food: FoodDefinitions) : ViewModel() {

    constructor() : this(ObjectsPool.single(Asynchronous::class.java), ObjectsPool.single(FoodDefinitions::class.java))

    fun filtered(criteria: String, callback: Callback<List<FoodDefinition>>) {
        this.asynchronous.execute({
            val filtered: MutableList<FoodDefinition> = ArrayList()
            filtered
        }, callback)
    }

    fun all(callback: Callback<List<FoodDefinition>>) {

    }

    fun refresh() {

    }
}