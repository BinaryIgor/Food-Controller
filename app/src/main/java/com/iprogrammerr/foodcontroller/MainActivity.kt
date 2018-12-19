package com.iprogrammerr.foodcontroller

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.iprogrammerr.foodcontroller.view.RootView
import com.iprogrammerr.foodcontroller.view.fragment.MenuFragment

class MainActivity : AppCompatActivity(), RootView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            replace(MenuFragment(), false)
        }
    }

    override fun replace(fragment: Fragment, toBackStack: Boolean) {
        val transaction = this.supportFragmentManager.beginTransaction()
            .replace(R.id.fragment, fragment)
        if (toBackStack) {
            transaction.addToBackStack(fragment.javaClass.simpleName)
        }
        transaction.commit()
    }

    override fun changeTitle(title: String) {
        this.supportActionBar?.title = title
    }
}
