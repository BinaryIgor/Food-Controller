package com.iprogrammerr.foodcontroller.viewmodel

import android.arch.lifecycle.ViewModel
import com.iprogrammerr.foodcontroller.ObjectsPool
import com.iprogrammerr.foodcontroller.model.Asynchronous
import com.iprogrammerr.foodcontroller.model.day.Days
import com.iprogrammerr.foodcontroller.model.goals.Goals
import com.iprogrammerr.foodcontroller.model.result.Callback
import kotlin.math.roundToInt

class GoalsViewModel(
    private val asynchronous: Asynchronous,
    private val days: Days,
    private val goals: Goals
) : ViewModel() {

    constructor() : this(
        ObjectsPool.single(Asynchronous::class.java),
        ObjectsPool.single(Days::class.java),
        ObjectsPool.single(Goals::class.java)
    )

    fun goals() = this.goals

    fun update(update: (Goals) -> Unit, callback: Callback<Boolean>) {
        this.asynchronous.execute({
            update(this.goals)
            val date = System.currentTimeMillis()
            if (this.days.exists(date)) {
                val day = this.days.day(date)
                if (day.weight() != this.goals.weight().value()) {
                    day.changeWeight(this.goals.weight().value())
                }
                val goals = day.goals()
                if (goals.calories() != this.goals.calories().value() ||
                    goals.protein().roundToInt() != this.goals.protein().value()) {
                    day.changeGoals(this.goals.calories().value(), this.goals.protein().value())
                }
            }
            true
        }, callback)
    }
}