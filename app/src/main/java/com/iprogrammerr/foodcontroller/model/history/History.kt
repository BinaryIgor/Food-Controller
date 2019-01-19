package com.iprogrammerr.foodcontroller.model.history

import java.util.*

interface History {

    fun years(): List<Int>

    fun months(year: Int): List<Calendar>
}