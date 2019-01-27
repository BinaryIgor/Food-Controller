package com.iprogrammerr.foodcontroller.model.format

import java.text.DateFormat
import java.text.SimpleDateFormat

class UiFormats(private val dateFormat: DateFormat) : Formats {

    constructor() : this(SimpleDateFormat("dd.MM.yyyy"))

    override fun date() = this.dateFormat
}