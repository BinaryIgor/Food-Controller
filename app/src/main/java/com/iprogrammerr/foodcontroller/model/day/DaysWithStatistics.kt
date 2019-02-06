package com.iprogrammerr.foodcontroller.model.day

import com.iprogrammerr.foodcontroller.model.NutritionalValues

class DaysWithStatistics(val days: List<Day>) {

    private val goals: NutritionalValues by lazy {
        var calories = 0
        var protein = 0.0
        for (d in this.days) {
            val goals = d.goals()
            calories += goals.calories()
            protein += goals.protein()
        }
        if (this.days.isNotEmpty()) {
            calories /= this.days.size
            protein /= this.days.size
        }
        object : NutritionalValues {
            override fun calories() = calories

            override fun protein() = protein
        }
    }

    private val realized: NutritionalValues by lazy {
        var calories = 0
        var protein = 0.0
        for (d in this.days) {
            for (m in d.meals()) {
                val values = m.nutritionalValues()
                calories += values.calories()
                protein += values.protein()
            }
        }
        if (this.days.isNotEmpty()) {
            calories /= this.days.size
            protein /= this.days.size
        }
        object : NutritionalValues {
            override fun calories() = calories

            override fun protein() = protein
        }
    }

    fun goals() = this.goals

    fun realized() = this.realized
}