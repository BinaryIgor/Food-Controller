package com.iprogrammerr.foodcontroller.model.goals

import android.content.SharedPreferences

class PreferencesGoals(
    private val source: SharedPreferences,
    private val defaultWeight: Double,
    private val defaultCalories: Int,
    private val defaultProtein: Int
) : Goals {

    private val weight by lazy {
        PreferencesWeightGoal(this.source, this.defaultWeight)
    }

    private val calories by lazy {
        PreferencesCaloriesGoal(this.source, this.defaultCalories)
    }

    private val protein by lazy {
        PreferencesProteinGoal(this.source, this.defaultProtein)
    }

    override fun weight() = this.weight

    override fun calories() = this.calories

    override fun protein() = this.protein
}