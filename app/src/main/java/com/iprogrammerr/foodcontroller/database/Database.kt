package com.iprogrammerr.foodcontroller.database

import android.content.ContentValues

interface Database {

    fun query(sql: String): Rows

    fun insert(table: String, values: ContentValues)

    fun update(table: String, whereClause: String, values: ContentValues)

    fun delete(table: String, whereClause: String)
}