package com.iprogrammerr.foodcontroller.model.day

import android.content.ContentValues
import com.iprogrammerr.foodcontroller.database.Database
import com.iprogrammerr.foodcontroller.model.NutritionalValues
import com.iprogrammerr.foodcontroller.model.meal.Meal

class DatabaseDay(
    private val id: Long,
    private val database: Database,
    private val date: Long,
    private var weight: Double,
    private var caloriesGoal: Int,
    private var proteinGoal: Int,
    private val meals: List<Meal>
) : Day {

    override fun id() = this.id

    override fun goals() = object : NutritionalValues {

        override fun calories() = this@DatabaseDay.caloriesGoal

        override fun protein() = this@DatabaseDay.proteinGoal.toDouble()
    }

    override fun date() = this.date

    override fun meals() = this.meals

    override fun weight() = this.weight

    override fun changeGoals(calories: Int, protein: Int) {
        val values = ContentValues()
        values.put("calories_goal", calories)
        values.put("protein_goal", protein)
        this.database.update("day", "id = ${this.id}", values)
        this.caloriesGoal = calories
        this.proteinGoal = protein
    }

    override fun changeWeight(weight: Double) {
        val values = ContentValues()
        values.put("weight", weight)
        this.database.update("day", "id = ${this.id}", values)
        this.weight = weight
    }
}