package com.iprogrammerr.foodcontroller.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.iprogrammerr.foodcontroller.model.StickyScalar

class FoodControllerDatabase(context: Context) :
    SQLiteOpenHelper(context, FoodControllerDatabase::javaClass.name, null, 1) {

    private val foodContract = StickyScalar {
        StringBuilder().append("CREATE TABLE food ").append("(")
            .append("id INTEGER PRIMARY KEY AUTOINCREMENT").append(",")
            .append("name TEXT NOT NULL UNIQUE").append(",")
            .append("calories INTEGER NOT NULL CHECK (calories > 0)").append(",")
            .append("protein INTEGER NOT NULL CHECK (protein >= 0)").append(",")
            .append("deleted INTEGER DEFAULT 0 CHECK (deleted >= 0 && deleted <= 1)")
            .append(")").toString()
    }

    private val portionContract = StickyScalar {
        StringBuilder().append("CREATE TABLE food_portion ").append("(")
            .append("id INTEGER PRIMARY KEY AUTOINCREMENT").append(",")
            .append("food_id INTEGER NOT NULL").append(",")
            .append("portion_id INTEGER NOT NULL").append(",")
            .append("FOREIGN KEY ")
            .append(")").toString()
    }

    override fun onCreate(db: SQLiteDatabase) {

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}