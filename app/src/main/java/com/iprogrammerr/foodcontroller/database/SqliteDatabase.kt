package com.iprogrammerr.foodcontroller.database

import android.content.ContentValues
import android.database.sqlite.SQLiteOpenHelper

class SqliteDatabase(private val origin: SQLiteOpenHelper) : Database {

    override fun query(sql: String) = DatabaseRows(this.origin.readableDatabase.rawQuery(sql, null))

    override fun insert(table: String, values: ContentValues) =
        this.origin.writableDatabase.insert(table, null, values)

    override fun update(table: String, whereClause: String, values: ContentValues) {
        this.origin.writableDatabase.update(table, values, whereClause, null)
    }

    override fun delete(table: String, whereClause: String) {
        this.origin.writableDatabase.delete(table, whereClause, null)
    }
}