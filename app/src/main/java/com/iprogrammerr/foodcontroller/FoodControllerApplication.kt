package com.iprogrammerr.foodcontroller

import android.app.Application
import com.iprogrammerr.foodcontroller.database.Database
import com.iprogrammerr.foodcontroller.database.FoodControllerDatabase
import com.iprogrammerr.foodcontroller.database.SqliteDatabase
import com.iprogrammerr.foodcontroller.model.category.Categories
import com.iprogrammerr.foodcontroller.model.category.DatabaseCategories
import com.iprogrammerr.foodcontroller.model.day.DatabaseDays
import com.iprogrammerr.foodcontroller.model.day.Days
import com.iprogrammerr.foodcontroller.model.day.LastWeight
import com.iprogrammerr.foodcontroller.model.day.Weight
import com.iprogrammerr.foodcontroller.model.food.DatabaseFoodDefinitions
import com.iprogrammerr.foodcontroller.model.food.FoodDefinitions
import com.iprogrammerr.foodcontroller.pool.ObjectsPool
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class FoodControllerApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        val database: Database =
            SqliteDatabase(FoodControllerDatabase(this, this.resources.openRawResource(R.raw.database).use { i ->
                val reader = BufferedReader(InputStreamReader(i))
                val lines = StringBuilder()
                var line = reader.readLine()
                while (line != null) {
                    lines.append(line)
                    line = reader.readLine()
                }
                JSONObject(lines.toString())
            }))
        val executor: Executor = Executors.newCachedThreadPool()
        val days = DatabaseDays(database)
        val categories = DatabaseCategories(database)
        ObjectsPool.add(Executor::class.java, executor)
        ObjectsPool.add(Database::class.java, database)
        ObjectsPool.add(Days::class.java, days)
        ObjectsPool.add(Weight::class.java, LastWeight(database, 65.0))
        ObjectsPool.add(Categories::class.java, categories)
        ObjectsPool.add(FoodDefinitions::class.java, DatabaseFoodDefinitions(database))
    }
}