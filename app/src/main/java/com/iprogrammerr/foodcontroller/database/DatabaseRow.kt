package com.iprogrammerr.foodcontroller.database

import android.database.Cursor

class DatabaseRow(private val cursor: Cursor) : Row {

    override fun int(name: String) = this.cursor.getInt(this.cursor.getColumnIndex(name))

    override fun long(name: String) = this.cursor.getLong(this.cursor.getColumnIndex(name))

    override fun double(name: String) = this.cursor.getDouble(this.cursor.getColumnIndex(name))

    override fun string(name: String) = this.cursor.getString(this.cursor.getColumnIndex(name))
}