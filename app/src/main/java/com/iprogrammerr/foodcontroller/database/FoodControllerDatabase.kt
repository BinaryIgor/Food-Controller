package com.iprogrammerr.foodcontroller.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class FoodControllerDatabase(context: Context) :
    SQLiteOpenHelper(context, FoodControllerDatabase::javaClass.name, null, 1) {

    private val foodDefinitionContract = StringBuilder()
        .append("CREATE TABLE food_definition")
        .append("(")
        .append("id INTEGER PRIMARY KEY AUTOINCREMENT")
        .append(",")
        .append("name TEXT NOT NULL UNIQUE")
        .append(",")
        .append("calories INTEGER NOT NULL CHECK(calories > 0)")
        .append(",")
        .append("protein INTEGER NOT NULL CHECK(protein >= 0)")
        .append(",")
        .append("deleted INTEGER DEFAULT 0 CHECK(deleted >= 0 && deleted <= 1)")
        .append(")")
        .toString()

    private val foodContract = StringBuilder()
        .append("CREATE TABLE food")
        .append("(")
        .append("id INTEGER PRIMARY KEY AUTOINCREMENT")
        .append(",")
        .append("definition_id INTEGER NOT NULL")
        .append(",")
        .append("weight INTEGER NOT NULL CHECK(weight > 0)")
        .append(",")
        .append("UNIQUE(food_id, weight)")
        .append(",")
        .append("FOREIGN KEY(definition_id) REFERENCES food_definition(id) ON DELETE CASCADE")
        .append(")")
        .toString()

    //hour range for allowing eating after midnight and still have proper meals order
    private val mealContract = StringBuilder()
        .append("CREATE TABLE meal")
        .append("(")
        .append("id INTEGER PRIMARY KEY AUTOINCREMENT")
        .append(",")
        .append("day_id INTEGER NOT NULL")
        .append(",")
        .append("hour INTEGER NOT NULL CHECK(hour >= 0 && hour <= 36")
        .append(",")
        .append("minutes INTEGER NOT NULL CHECK(minutes >= 0 && minutes < 60)")
        .append(",")
        .append("FOREIGN KEY(day_id) REFERENCES day(id) ON DELETE CASCADE")
        .append(")")
        .toString()

    private val dayContract = StringBuilder()
        .append("CREATE TABLE day")
        .append("(")
        .append("id INTEGER PRIMARY KEY AUTOINCREMENT")
        .append(",")
        .append("date INTEGER NOT NULL")
        .append(",")
        .append("calories_goal INTEGER NOT NULL CHECK(calories_goal >= 0)")
        .append(",")
        .append("protein_goal INTEGER NOT NULL CHECK(protein_goal >= 0)")
        .append(")")
        .toString()

    private val foodMealContract = StringBuilder()
        .append("CREATE TABLE food_meal")
        .append("(")
        .append("id INTEGER PRIMARY KEY AUTOINCREMENT")
        .append(",")
        .append("food_id INTEGER NOT NULL")
        .append(",")
        .append("meal_id INTEGER NOT NULL")
        .append(",")
        .append("UNIQUE(food_id, meal_id)")
        .append(",")
        .append("FOREIGN KEY (food_id) REFERENCES food(id) ON DELETE CASCADE")
        .append(",")
        .append("FOREIGN KEY (meal_id) REFERENCES meal(id) ON DELETE CASCADE")
        .append(")").toString()

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(this.foodDefinitionContract)
        db.execSQL(this.foodContract)
        db.execSQL(this.mealContract)
        db.execSQL(this.foodMealContract)
        db.execSQL(this.dayContract)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}