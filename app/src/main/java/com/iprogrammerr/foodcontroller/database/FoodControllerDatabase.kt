package com.iprogrammerr.foodcontroller.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.iprogrammerr.foodcontroller.database.contract.Contract

class FoodControllerDatabase(context: Context, private val contracts: List<Contract>) :
    SQLiteOpenHelper(context, FoodControllerDatabase::javaClass.name, null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        this.contracts.forEach { db.execSQL(it.createSql()) }
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}