package com.iprogrammerr.foodcontroller.model

import android.widget.EditText

class IntFromView(private val source: EditText, private val default: Int) : Scalar<Int> {

    constructor(source: EditText) : this(source, 0)

    override fun value(): Int {
        return try {
            this.source.text.toString().trim().toInt()
        } catch (e: Exception) {
            this.default
        }
    }
}