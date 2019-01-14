package com.iprogrammerr.foodcontroller.viewmodel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.iprogrammerr.foodcontroller.ObjectsPool
import com.iprogrammerr.foodcontroller.model.Asynchronous
import com.iprogrammerr.foodcontroller.model.food.Food
import com.iprogrammerr.foodcontroller.model.food.FoodDefinition
import com.iprogrammerr.foodcontroller.model.food.FoodDefinitions
import com.iprogrammerr.foodcontroller.model.food.Portions
import com.iprogrammerr.foodcontroller.model.meal.Meals
import com.iprogrammerr.foodcontroller.model.result.Callback
import kotlin.math.roundToInt

class FoodPortionViewModel(
    private val asynchronous: Asynchronous,
    private val definitionId: Long,
    private val definitions: FoodDefinitions,
    private val portions: Portions,
    private val meals: Meals
) : ViewModel() {

    private val definition by lazy {
        this.definitions.definition(this.definitionId)
    }

    companion object {
        fun factory(definitionId: Long) = object : ViewModelProvider.NewInstanceFactory() {

            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(clazz: Class<T>): T = when {
                clazz.isAssignableFrom(FoodPortionViewModel::class.java) ->
                    FoodPortionViewModel(
                        ObjectsPool.single(Asynchronous::class.java),
                        definitionId,
                        ObjectsPool.single(FoodDefinitions::class.java),
                        ObjectsPool.single(Portions::class.java),
                        ObjectsPool.single(Meals::class.java)
                    ) as T
                else -> throw Exception(
                    "$clazz is not a ${FoodPortionViewModel::class.java.simpleName}"
                )
            }
        }
    }

    fun definition(callback: Callback<FoodDefinition>) {
        this.asynchronous.execute({ this.definition }, callback)
    }

    fun calories(weight: Int) =
        (this.definition.calories() * (weight / 100.0)).roundToInt()

    fun add(weight: Int, mealId: Long, callback: Callback<Boolean>) {
        this.asynchronous.execute({
            val previous = previousMealFood(mealId)
            if (previous.isEmpty()) {
                if (!this.portions.exists(this.definitionId, weight)) {
                    this.portions.create(this.definitionId, weight)
                }
                this.portions.add(this.definitionId, weight, mealId)
            } else {
                var newWeight = weight
                for (f in previous) {
                    newWeight += f.weight()
                }
                if (!this.portions.exists(this.definitionId, newWeight)) {
                    this.portions.create(this.definitionId, newWeight)
                }
                this.portions.update(previous[0].id(), mealId, this.definitionId, newWeight)
            }
            true
        }, callback)
    }

    fun update(weight: Int, mealId: Long, callback: Callback<Boolean>) {
        this.asynchronous.execute({
            if (!this.portions.exists(this.definitionId, weight)) {
                this.portions.create(this.definitionId, weight)
            }
            val previous = previousMealFood(mealId)
            if (previous.isEmpty()) {
                this.portions.add(this.definitionId, weight, mealId)
            } else {
                this.portions.update(previous[0].id(), mealId, this.definitionId, weight)
            }
            true
        }, callback)
    }

    private fun previousMealFood(id: Long): List<Food> {
        val previous = ArrayList<Food>()
        for (f in this.meals.meal(id).food()) {
            if (this.definition.name() == f.name()) {
                previous.add(f)
            }
        }
        return previous
    }
}