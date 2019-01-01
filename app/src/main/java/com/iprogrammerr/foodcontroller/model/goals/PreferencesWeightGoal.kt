package com.iprogrammerr.foodcontroller.model.goals

import android.content.SharedPreferences

class PreferencesWeightGoal(private val source: SharedPreferences, private val default: Double) : Goal<Double> {

    override fun value() = this.source.getFloat("weight", this.default.toFloat()).toDouble()

    override fun change(value: Double) {
        this.source.edit().putFloat("weight", value.toFloat()).apply()
    }
}