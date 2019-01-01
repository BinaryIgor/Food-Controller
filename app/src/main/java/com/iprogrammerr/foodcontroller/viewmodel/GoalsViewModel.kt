package com.iprogrammerr.foodcontroller.viewmodel

import android.arch.lifecycle.ViewModel
import com.iprogrammerr.foodcontroller.model.goals.Goals

class GoalsViewModel(private val goals: Goals) : ViewModel() {

    fun goals() = this.goals
}