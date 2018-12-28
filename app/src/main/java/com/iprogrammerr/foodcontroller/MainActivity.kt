package com.iprogrammerr.foodcontroller

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.iprogrammerr.foodcontroller.databinding.ActivityMainBinding
import com.iprogrammerr.foodcontroller.view.RootView
import com.iprogrammerr.foodcontroller.view.fragment.MenuFragment

class MainActivity : AppCompatActivity(), RootView {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        if (savedInstanceState == null) {
            replace(MenuFragment(), false)
        }
        this.binding.back.setOnClickListener {
            if (this.supportFragmentManager.backStackEntryCount > 0)
                onBackPressed()
        }
        this.supportFragmentManager.addOnBackStackChangedListener { resolveBackVisibility() }
    }

    override fun onStart() {
        super.onStart()
        resolveBackVisibility()
    }

    private fun resolveBackVisibility() {
        if (this.supportFragmentManager.backStackEntryCount == 0) {
            this.binding.back.visibility = View.INVISIBLE
        } else {
            this.binding.back.visibility = View.VISIBLE
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
        runOnUiThread { this.binding.toolbarTitle.text = title }
    }

    override fun runOnMain(runnable: () -> Unit) {
        runOnUiThread(runnable)
    }
}
