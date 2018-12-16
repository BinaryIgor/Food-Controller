package com.iprogrammerr.foodcontroller

import android.app.Application
import android.content.ContentValues
import android.database.Cursor
import com.iprogrammerr.foodcontroller.database.FoodControllerDatabase
import com.iprogrammerr.foodcontroller.database.SqliteDatabase

class FoodControllerApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        val database = SqliteDatabase(FoodControllerDatabase(this, ArrayList()))
    }
}