package com.iprogrammerr.foodcontroller.model.goals

interface Goals {

    fun weight(): Goal<Double>

    fun calories(): Goal<Int>

    fun protein(): Goal<Int>
}