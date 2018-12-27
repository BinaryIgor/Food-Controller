package com.iprogrammerr.foodcontroller.viewmodel

import android.arch.lifecycle.ViewModel
import com.iprogrammerr.foodcontroller.model.category.Categories
import com.iprogrammerr.foodcontroller.model.category.Category
import com.iprogrammerr.foodcontroller.model.result.Result
import com.iprogrammerr.foodcontroller.model.result.ResultValue
import java.util.concurrent.Executor

class CategoriesViewModel(private val executor: Executor, private val source: Categories) : ViewModel() {

    private val categories by lazy {
        try {
            ResultValue(this.source.all())
        } catch (e: Exception) {
            ResultValue<List<Category>>(e)
        }
    }

    fun categories(result: (Result<List<Category>>) -> Unit) {
        this.executor.execute { result(this.categories) }
    }
}