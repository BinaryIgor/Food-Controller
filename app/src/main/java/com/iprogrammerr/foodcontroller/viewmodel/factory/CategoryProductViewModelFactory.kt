package com.iprogrammerr.foodcontroller.viewmodel.factory

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.iprogrammerr.foodcontroller.model.category.Category
import com.iprogrammerr.foodcontroller.viewmodel.CategoryProductsViewModel
import java.util.concurrent.Executor

class CategoryProductViewModelFactory(private val executor: Executor, private val category: Category) :
    ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(clazz: Class<T>): T = when {
        clazz.isAssignableFrom(CategoryProductsViewModel::class.java) -> clazz.cast(
            CategoryProductsViewModel(
                this.executor,
                this.category
            )
        )
        else -> throw Exception("$clazz is not a ${CategoryProductsViewModel::class.java.simpleName}")
    }
}