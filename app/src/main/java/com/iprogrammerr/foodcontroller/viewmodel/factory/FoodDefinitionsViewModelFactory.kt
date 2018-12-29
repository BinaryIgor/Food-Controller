package com.iprogrammerr.foodcontroller.viewmodel.factory

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.iprogrammerr.foodcontroller.model.food.FoodDefinitions
import com.iprogrammerr.foodcontroller.viewmodel.CategoryFoodDefinitionsViewModel
import com.iprogrammerr.foodcontroller.viewmodel.FoodDefinitionViewModel
import java.util.concurrent.Executor

class FoodDefinitionsViewModelFactory(private val executor: Executor, private val definitions: FoodDefinitions) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(clazz: Class<T>): T = when {
        clazz.isAssignableFrom(FoodDefinitionViewModel::class.java) ->
            FoodDefinitionViewModel(
                this.executor,
                this.definitions
            ) as T
        else -> throw Exception("$clazz is not a ${CategoryFoodDefinitionsViewModel::class.java.simpleName}")
    }
}