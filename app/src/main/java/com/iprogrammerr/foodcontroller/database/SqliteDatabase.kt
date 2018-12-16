package com.iprogrammerr.foodcontroller.database

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteOpenHelper

class SqliteDatabase(private val origin: SQLiteOpenHelper) : Database {

    override fun query(sql: String, mapping: (Cursor) -> Unit) {
        val cursor = this.origin.readableDatabase.rawQuery(sql, null)
        cursor.moveToNext()
        mapping(cursor)
        cursor.close()
    }

    override fun insert(table: String, values: ContentValues) {
        this.origin.writableDatabase.insert(table, null, values)
    }

    override fun update(table: String, whereClause: String, values: ContentValues) {
        this.origin.writableDatabase.update(table, values, whereClause, null)
    }

    override fun delete(table: String, whereClause: String) {
        this.origin.writableDatabase.delete(table, whereClause, null)
    }
}