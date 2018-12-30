package com.iprogrammerr.foodcontroller.viewmodel.factory

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.iprogrammerr.foodcontroller.model.category.Categories
import com.iprogrammerr.foodcontroller.model.food.FoodDefinitions
import com.iprogrammerr.foodcontroller.viewmodel.MoveDeleteFoodViewModel
import java.util.concurrent.Executor

class MoveDeleteFoodViewModelFactory(
    private val executor: Executor,
    private val categories: Categories,
    private val definitions: FoodDefinitions
) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(clazz: Class<T>): T = when {
        clazz.isAssignableFrom(MoveDeleteFoodViewModel::class.java) ->
            MoveDeleteFoodViewModel(
                this.executor,
                this.categories,
                this.definitions
            ) as T
        else -> throw Exception("$clazz is not a ${MoveDeleteFoodViewModel::class.java.simpleName}")
    }
}