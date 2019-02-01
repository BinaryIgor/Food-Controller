package com.iprogrammerr.foodcontroller.model.format

import java.text.DateFormat
import java.text.SimpleDateFormat

class UiFormats(
    private val dateFormat: DateFormat,
    private val numberFormat: Format<Double>
) : Formats {

    constructor() : this(
        SimpleDateFormat("dd.MM.yyyy"),
        RoundedFormat(1)
    )

    override fun date() = this.dateFormat

    override fun number() = this.numberFormat
}