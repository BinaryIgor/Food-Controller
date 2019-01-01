package com.iprogrammerr.foodcontroller.model.goals

import android.content.SharedPreferences

class PreferencesProteinGoal(private val source: SharedPreferences, private val default: Int) : Goal<Int> {

    override fun value() = this.source.getInt("protein", this.default)

    override fun change(value: Int) {
        this.source.edit().putInt("protein", value).apply()
    }
}