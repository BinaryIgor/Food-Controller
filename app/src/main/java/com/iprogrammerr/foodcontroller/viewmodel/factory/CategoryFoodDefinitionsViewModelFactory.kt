package com.iprogrammerr.foodcontroller.viewmodel.factory

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.iprogrammerr.foodcontroller.model.category.Category
import com.iprogrammerr.foodcontroller.viewmodel.CategoryFoodDefinitionsViewModel
import java.util.concurrent.Executor

class CategoryFoodDefinitionsViewModelFactory(private val executor: Executor, private val category: Category) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(clazz: Class<T>): T = when {
        clazz.isAssignableFrom(CategoryFoodDefinitionsViewModel::class.java) ->
            CategoryFoodDefinitionsViewModel(
                this.executor,
                this.category
            ) as T
        else -> throw Exception("$clazz is not a ${CategoryFoodDefinitionsViewModel::class.java.simpleName}")
    }
}