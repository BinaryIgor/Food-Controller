package com.iprogrammerr.foodcontroller.model.day

import android.content.ContentValues
import com.iprogrammerr.foodcontroller.database.Database
import com.iprogrammerr.foodcontroller.model.NutritionalValues
import com.iprogrammerr.foodcontroller.model.food.DatabaseFood
import com.iprogrammerr.foodcontroller.model.food.Food
import com.iprogrammerr.foodcontroller.model.meal.DatabaseMeal
import com.iprogrammerr.foodcontroller.model.meal.Meal

class DatabaseDay(
    private val id: Long,
    private val database: Database
) : Day {

    private val attributes: MutableMap<String, Any> = HashMap()
    private val meals: MutableList<Meal> = ArrayList()
    private var mealsQueried = false

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
        this.mealsQueried = true
    }

    override fun id() = this.id

    override fun goals(): NutritionalValues {
        if (this.attributes.isEmpty()) {
            loadAttributes()
        }
        val calories = this.attributes["caloriesGoal"] as Int
        val protein = this.attributes["proteinGoal"] as Int
        return object : NutritionalValues {

            override fun calories() = calories

            override fun protein() = protein.toDouble()
        }
    }

    override fun date(): Long {
        if (this.attributes.isEmpty()) {
            loadAttributes()
        }
        return this.attributes["date"] as Long
    }

    override fun meals(): List<Meal> {
        if (!this.mealsQueried) {
            loadMeals()
        }
        return this.meals
    }

    override fun weight(): Double {
        if (this.attributes.isEmpty()) {
            loadAttributes()
        }
        return this.attributes["weight"] as Double
    }

    override fun changeWeight(weight: Double) {
        val values = ContentValues()
        values.put("weight", weight)
        this.database.update("day", "id = ${this.id}", values)
        this.attributes["weight"] = weight
    }

    private fun loadAttributes() {
        this.database.query("SELECT * FROM day WHERE id = ${this.id}")
            .use { rs ->
                val r = rs.next()
                this.attributes["date"] = r.long("date")
                this.attributes["weight"] = r.long("weight")
                this.attributes["caloriesGoal"] = r.long("calories_goal")
                this.attributes["proteinGoal"] = r.long("protein_goal")
            }
    }

    private fun loadMeals() {
        this.database.query(
            StringBuilder()
                .append(
                    "SELECT m.id as m_id, m.time, f.id as f_id, f.weight, fd.name, fd.calories, fd.protein ")
                .append("FROM meal m INNER JOIN food_meal fm ON m.id = fm.meal_id ")
                .append("INNER JOIN food f ON f.id = fm.food_id ")
                .append("INNER JOIN food_definition fd on f.definition_id = fd.id ")
                .append("WHERE m.day_id = ${this.id}").toString()
        ).use { rs ->
            this.meals.clear()
            var row = rs.next()
            do {
                var hasNext = false
                val mealId = row.long("m_id")
                val time = row.long("time")
                val food: MutableList<Food> = ArrayList()
                do {
                    if (row.long("m_id") != mealId) {
                        this.meals.add(DatabaseMeal(mealId, this.database, time, food))
                        break
                    }
                    food.add(
                        DatabaseFood(
                            row.long("f_id"), this.database, row.string("name"),
                            row.int("f_weight"), row.int("protein"),
                            row.double("calories")
                        )
                    )
                    row = rs.next()
                    hasNext = rs.hasNext()
                } while (hasNext)
            } while (hasNext)
            this.mealsQueried = true
        }
    }
}