package com.iprogrammerr.foodcontroller

import android.app.Application

class FoodControllerApplication : Application() {

    override fun onCreate() {
        val version = 0.1
        System.out.print("Starting kotlin app ${version}")
        super.onCreate()
    }
}