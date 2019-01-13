package com.iprogrammerr.foodcontroller.model

import android.widget.EditText
import com.iprogrammerr.foodcontroller.model.scalar.IntFromView
import kotlin.math.roundToInt

class NutritionalValuesFromView(
    private val calories: Int,
    private val protein: Double,
    private val weight: IntFromView
) : NutritionalValues {

    constructor(calories: Int, protein: Double, weight: EditText) : this(
        calories, protein, IntFromView(weight)
    )

    override fun calories() =
        (this.calories * (this.weight.value() / 100.0)).roundToInt()

    override fun protein() =
        this.protein * (this.weight.value() / 100.0)
}