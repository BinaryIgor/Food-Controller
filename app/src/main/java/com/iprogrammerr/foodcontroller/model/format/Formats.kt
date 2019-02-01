package com.iprogrammerr.foodcontroller.model.format

import java.text.DateFormat

interface Formats {

    fun date(): DateFormat

    fun number(): Format<Double>
}