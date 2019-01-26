package com.iprogrammerr.foodcontroller.model.meal

import android.content.ContentValues
import com.iprogrammerr.foodcontroller.database.Database
import com.iprogrammerr.foodcontroller.model.NutritionalValues
import com.iprogrammerr.foodcontroller.model.food.Food
import kotlin.math.roundToInt

class DatabaseMeal(
    private val id: Long,
    private val database: Database,
    private var time: Long,
    food: List<Food>
) : Meal {

    private val food = ArrayList<Food>(food)

    override fun id() = this.id

    override fun time() = this.time

    override fun changeTime(time: Long) {
        val values = ContentValues()
        values.put("time", time)
        this.database.update("meal", "id = ${this.id}", values)
        this.time = time
    }

    override fun food() = this.food

    override fun nutritionalValues(): NutritionalValues {
        var calories = 0.0
        var protein = 0.0
        for (f in this.food) {
            val v = f.values()
            calories += v.calories()
            protein += v.protein()
        }
        return object : NutritionalValues {

            override fun calories() = calories.roundToInt()

            override fun protein() = protein
        }
    }

    override fun removeFood(id: Long) {
        this.database.delete(
            "food_meal", "meal_id = ${this.id} AND food_id = $id"
        )
        for (f in this.food) {
            if (f.id() == id) {
                this.food.remove(f)
                break
            }
        }
    }
}