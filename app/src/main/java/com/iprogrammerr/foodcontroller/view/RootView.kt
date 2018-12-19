package com.iprogrammerr.foodcontroller.view

import android.support.v4.app.Fragment

interface RootView {

    fun replace(fragment: Fragment, toBackStack: Boolean)

    fun changeTitle(title: String)
}