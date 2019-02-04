package com.iprogrammerr.foodcontroller

import android.app.Application
import android.preference.PreferenceManager
import com.iprogrammerr.foodcontroller.database.Database
import com.iprogrammerr.foodcontroller.database.FoodControllerDatabase
import com.iprogrammerr.foodcontroller.database.SqliteDatabase
import com.iprogrammerr.foodcontroller.model.Asynchronous
import com.iprogrammerr.foodcontroller.model.ReturningAsynchronous
import com.iprogrammerr.foodcontroller.model.category.Categories
import com.iprogrammerr.foodcontroller.model.category.DatabaseCategories
import com.iprogrammerr.foodcontroller.model.day.DatabaseDays
import com.iprogrammerr.foodcontroller.model.day.Days
import com.iprogrammerr.foodcontroller.model.day.LastWeight
import com.iprogrammerr.foodcontroller.model.day.Weight
import com.iprogrammerr.foodcontroller.model.food.DatabaseFoodDefinitions
import com.iprogrammerr.foodcontroller.model.food.DatabasePortions
import com.iprogrammerr.foodcontroller.model.food.FoodDefinitions
import com.iprogrammerr.foodcontroller.model.food.Portions
import com.iprogrammerr.foodcontroller.model.format.Formats
import com.iprogrammerr.foodcontroller.model.format.UiFormats
import com.iprogrammerr.foodcontroller.model.goals.Goals
import com.iprogrammerr.foodcontroller.model.goals.PreferencesGoals
import com.iprogrammerr.foodcontroller.model.history.DatabaseHistory
import com.iprogrammerr.foodcontroller.model.history.History
import com.iprogrammerr.foodcontroller.model.meal.DatabaseMeals
import com.iprogrammerr.foodcontroller.model.meal.Meals
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.concurrent.Executors

class FoodControllerApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        val database: Database =
            SqliteDatabase(FoodControllerDatabase(this,
                this.resources.openRawResource(R.raw.database).use { i ->
                    val reader = BufferedReader(InputStreamReader(i))
                    val lines = StringBuilder()
                    var line = reader.readLine()
                    while (line != null) {
                        lines.append(line)
                        line = reader.readLine()
                    }
                    JSONObject(lines.toString())
                }))
        val asynchronous = ReturningAsynchronous(Executors.newCachedThreadPool())
        val days = DatabaseDays(database)
        val categories = DatabaseCategories(database)
        ObjectsPool.add(Asynchronous::class.java, asynchronous)
        ObjectsPool.add(Database::class.java, database)
        ObjectsPool.add(Days::class.java, days)
        ObjectsPool.add(Weight::class.java, LastWeight(database, 70.0))
        ObjectsPool.add(Meals::class.java, DatabaseMeals(database))
        ObjectsPool.add(History::class.java, DatabaseHistory(database))
        ObjectsPool.add(Categories::class.java, categories)
        ObjectsPool.add(FoodDefinitions::class.java, DatabaseFoodDefinitions(database))
        ObjectsPool.add(Portions::class.java, DatabasePortions(database))
        ObjectsPool.add(
            Goals::class.java,
            PreferencesGoals(PreferenceManager.getDefaultSharedPreferences(this), 70.0, 2500, 100)
        )
        ObjectsPool.add(Formats::class.java, UiFormats())
    }
}