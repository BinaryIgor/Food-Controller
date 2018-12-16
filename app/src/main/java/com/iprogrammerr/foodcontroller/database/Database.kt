package com.iprogrammerr.foodcontroller.database

import android.content.ContentValues
import android.database.Cursor

interface Database {

    fun query(sql: String, mapping: (Cursor) -> Unit)

    fun insert(table: String, values: ContentValues)

    fun update(table: String, whereClause: String, values: ContentValues)

    fun delete(table: String, whereClause: String)
}