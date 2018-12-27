package com.iprogrammerr.foodcontroller.database

interface Row {

    fun int(name: String): Int

    fun long(name: String): Long

    fun double(name: String): Double

    fun string(name: String): String
}