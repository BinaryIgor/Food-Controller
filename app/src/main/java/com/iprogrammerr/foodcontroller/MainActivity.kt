package com.iprogrammerr.foodcontroller

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.iprogrammerr.foodcontroller.view.fragment.MenuFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.supportFragmentManager.beginTransaction().replace(R.id.fragment, MenuFragment()).commit()
    }
}
