package com.iprogrammerr.foodcontroller.database

interface Rows : AutoCloseable {

    fun current(): Row

    fun next(): Row

    fun hasNext(): Boolean
}