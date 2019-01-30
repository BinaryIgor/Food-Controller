package com.iprogrammerr.foodcontroller.view.items

interface WithActionTarget<T> {
    fun hit(item: T, action: Action)

    enum class Action {
        DETAILS, EDIT, DELETE
    }
}