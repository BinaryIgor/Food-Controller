package com.iprogrammerr.foodcontroller

import android.app.Application
import com.iprogrammerr.foodcontroller.database.FoodControllerDatabase

class FoodControllerApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        val database = FoodControllerDatabase(this, ArrayList())
    }
}