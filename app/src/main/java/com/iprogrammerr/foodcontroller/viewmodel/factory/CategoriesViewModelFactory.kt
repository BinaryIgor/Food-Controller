package com.iprogrammerr.foodcontroller.viewmodel.factory

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.iprogrammerr.foodcontroller.model.category.Categories
import com.iprogrammerr.foodcontroller.viewmodel.CategoriesViewModel
import java.util.concurrent.Executor

class CategoriesViewModelFactory(private val executor: Executor, private val categories: Categories) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(clazz: Class<T>): T = when {
        clazz.isAssignableFrom(CategoriesViewModel::class.java) ->
            CategoriesViewModel(
                this.executor,
                this.categories
            ) as T
        else -> throw Exception("$clazz is not a ${CategoriesViewModel::class.java.simpleName}")
    }
}