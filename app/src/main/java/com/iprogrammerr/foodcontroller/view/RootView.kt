package com.iprogrammerr.foodcontroller.view

import android.support.v4.app.Fragment
import com.iprogrammerr.foodcontroller.view.message.Message

interface RootView {

    fun replace(fragment: Fragment, toBackStack: Boolean)

    fun changeTitle(title: String)

    fun propagate(message: Message)
}