package com.iprogrammerr.foodcontroller

import android.app.Application
import com.iprogrammerr.foodcontroller.database.Database
import com.iprogrammerr.foodcontroller.database.FoodControllerDatabase
import com.iprogrammerr.foodcontroller.database.SqliteDatabase
import com.iprogrammerr.foodcontroller.model.day.DatabaseDays
import com.iprogrammerr.foodcontroller.model.day.Days
import com.iprogrammerr.foodcontroller.model.day.LastWeight
import com.iprogrammerr.foodcontroller.model.day.Weight
import com.iprogrammerr.foodcontroller.pool.ObjectsPool
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class FoodControllerApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        val database: Database = SqliteDatabase(FoodControllerDatabase(this))
        val executor: Executor = Executors.newCachedThreadPool()
        val days = DatabaseDays(database)
        ObjectsPool.add(Executor::class.java, executor)
        ObjectsPool.add(Days::class.java, days)
        ObjectsPool.add(Weight::class.java, LastWeight(database, 65.0))
    }
}