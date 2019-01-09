package com.iprogrammerr.foodcontroller.view.message

interface MessageTarget {

    fun hit(message: Message)

    fun isInterested(message: Message): Boolean
}