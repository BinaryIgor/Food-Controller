package com.iprogrammerr.foodcontroller.viewmodel.factory

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.iprogrammerr.foodcontroller.model.goals.Goals
import com.iprogrammerr.foodcontroller.viewmodel.GoalsViewModel

class GoalsViewModelFactory(private val goals: Goals) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(clazz: Class<T>): T = when {
        clazz.isAssignableFrom(GoalsViewModel::class.java) ->
            GoalsViewModel(this.goals) as T
        else -> throw Exception("$clazz is not a ${GoalsViewModel::class.java.simpleName}")
    }
}