package com.iprogrammerr.foodcontroller.model.food

import com.iprogrammerr.foodcontroller.model.NutritionalValues
import kotlin.math.roundToInt

class ConstantFood(
    private val id: Long,
    private val name: String,
    private val weight: Int,
    private val calories: Int,
    private val protein: Double,
    private val definitionId: Long
) : Food {

    override fun id() = this.id

    override fun name() = this.name

    override fun weight() = this.weight

    override fun values(): NutritionalValues {
        return object : NutritionalValues {

            override fun calories() =
                ((weight() / 100.0) * this@ConstantFood.calories).roundToInt()

            override fun protein() =
                (weight() / 100.0) * this@ConstantFood.protein
        }
    }

    override fun definitionId() = this.definitionId
}