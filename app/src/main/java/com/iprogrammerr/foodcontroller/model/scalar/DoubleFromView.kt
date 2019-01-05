package com.iprogrammerr.foodcontroller.model.scalar

import android.widget.EditText

class DoubleFromView(private val source: EditText, private val default: Double) :
    Scalar<Double> {

    constructor(source: EditText) : this(source, -1.0)

    override fun value(): Double {
        return try {
            this.source.text.toString().trim().toDouble()
        } catch (e: Exception) {
            this.default
        }
    }
}