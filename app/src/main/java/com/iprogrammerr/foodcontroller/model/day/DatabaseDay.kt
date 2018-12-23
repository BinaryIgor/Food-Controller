package com.iprogrammerr.foodcontroller.model.day

import com.iprogrammerr.foodcontroller.database.Database
import com.iprogrammerr.foodcontroller.model.NutritionalValues
import com.iprogrammerr.foodcontroller.model.meal.Meal

class DatabaseDay(
    private val id: Long,
    private val database: Database
) : Day {

    private val attributes: MutableMap<String, Any> = HashMap()
    private val meals: MutableList<Meal> = ArrayList()

    constructor(
        id: Long,
        database: Database,
        date: Long,
        weight: Double,
        caloriesGoal: Int,
        proteinGoal: Int,
        meals: List<Meal>
    ) :
            this(id, database) {
        this.attributes["date"] = date
        this.attributes["weight"] = weight
        this.attributes["caloriesGoal"] = caloriesGoal
        this.attributes["proteinGoal"] = proteinGoal
        this.meals.addAll(meals)
    }


    override fun goals(): NutritionalValues {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun date(): Long {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun meals(): List<Meal> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun weight(): Double {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun addMeal(id: Long) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun removeMeal(id: Long) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}