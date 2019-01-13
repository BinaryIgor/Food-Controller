package com.iprogrammerr.foodcontroller.view.items

interface IdWithActionTarget {
    fun hit(id: Long, action: Action)

    enum class Action {
        EDIT, DELETE
    }
}