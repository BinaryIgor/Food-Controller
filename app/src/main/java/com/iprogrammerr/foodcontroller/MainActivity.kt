package com.iprogrammerr.foodcontroller

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.iprogrammerr.foodcontroller.databinding.ActivityMainBinding
import com.iprogrammerr.foodcontroller.view.DeletePopup
import com.iprogrammerr.foodcontroller.view.RootView
import com.iprogrammerr.foodcontroller.view.fragment.DayFragment
import com.iprogrammerr.foodcontroller.view.fragment.MealFragment
import com.iprogrammerr.foodcontroller.view.fragment.MenuFragment
import com.iprogrammerr.foodcontroller.view.message.Message
import com.iprogrammerr.foodcontroller.view.message.MessageTarget

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
        this.supportFragmentManager.addOnBackStackChangedListener {
            resolveBackVisibility()
            resolveMoreVisibility()
        }
        this.binding.more.setOnClickListener {
            DeletePopup(this.binding.toolbar) {
                propagate(Message.DELETE_DAY_CLICKED)
            }.show()
        }
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

    private fun resolveMoreVisibility() {
        val fragment = this.supportFragmentManager.findFragmentById(R.id.fragment)
        fragment?.let { f ->
            if (f.tag == DayFragment::class.java.simpleName) {
                this.binding.more.visibility = View.VISIBLE
            } else {
                this.binding.more.visibility = View.INVISIBLE
            }
        }
    }

    override fun replace(fragment: Fragment, toBackStack: Boolean) {
        val tag = fragment.javaClass.simpleName
        val transaction = this.supportFragmentManager.beginTransaction()
            .replace(R.id.fragment, fragment, tag)
        if (toBackStack) {
            transaction.addToBackStack(tag)
        }
        transaction.commit()
    }

    override fun changeTitle(title: String) {
        runOnUiThread { this.binding.toolbarTitle.text = title }
    }

    override fun propagate(message: Message) {
        for (i in 0 until this.supportFragmentManager.backStackEntryCount) {
            val fragment = this.supportFragmentManager
                .findFragmentByTag(
                    this.supportFragmentManager.getBackStackEntryAt(i).name
                )
            if (fragment is MessageTarget) {
                if (fragment.isInterested(message)) {
                    resolveMessage(message, fragment)
                    break
                }
            }
        }
    }

    private fun resolveMessage(message: Message, target: MessageTarget) {
        if (message == Message.PORTIONS_CHANGED) {
            this.supportFragmentManager.popBackStack(MealFragment::class.java.simpleName, 0)
        }
        target.hit(message)
    }
}
