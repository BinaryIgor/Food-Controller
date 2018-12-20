package com.iprogrammerr.foodcontroller.database

import android.content.ContentValues
import android.database.Cursor

interface Database {

    fun query(sql: String): Cursor

    fun insert(table: String, values: ContentValues)

    fun update(table: String, whereClause: String, values: ContentValues)

    fun delete(table: String, whereClause: String)
}