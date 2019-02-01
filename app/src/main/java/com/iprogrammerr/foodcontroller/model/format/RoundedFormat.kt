package com.iprogrammerr.foodcontroller.model.format

import kotlin.math.roundToInt

class RoundedFormat(private val places: Int) : Format<Double> {

    private val integerThreshold = 0.05

    override fun formatted(value: Double) =
        if (value - value.toInt() < this.integerThreshold) {
            value.roundToInt().toString()
        } else {
            String.format("%.${this.places}f", value)
        }
}