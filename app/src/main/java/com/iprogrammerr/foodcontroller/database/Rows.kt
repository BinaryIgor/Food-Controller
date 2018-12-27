package com.iprogrammerr.foodcontroller.database

interface Rows : AutoCloseable {

    fun next(): Row

    fun hasNext(): Boolean
}