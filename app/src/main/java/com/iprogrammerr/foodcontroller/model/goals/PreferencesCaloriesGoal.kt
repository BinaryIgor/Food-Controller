package com.iprogrammerr.foodcontroller.model.goals

import android.content.SharedPreferences

class PreferencesCaloriesGoal(private val source: SharedPreferences, private val default: Int) : Goal<Int> {

    override fun value() = this.source.getInt("calories", this.default)

    override fun change(value: Int) {
        this.source.edit().putInt("calories", value).apply()
    }
}