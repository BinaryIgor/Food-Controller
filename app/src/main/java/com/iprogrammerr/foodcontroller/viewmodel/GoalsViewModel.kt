package com.iprogrammerr.foodcontroller.viewmodel

import android.arch.lifecycle.ViewModel
import com.iprogrammerr.foodcontroller.model.goals.Goals
import com.iprogrammerr.foodcontroller.ObjectsPool

class GoalsViewModel(private val goals: Goals) : ViewModel() {

    constructor() : this(ObjectsPool.single(Goals::class.java))

    fun goals() = this.goals
}