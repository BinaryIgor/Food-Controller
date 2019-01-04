package com.iprogrammerr.foodcontroller.database

import android.database.Cursor

class DatabaseRow(private val cursor: Cursor) : Row {

    override fun int(name: String) = this.cursor.getInt(index(name))

    override fun long(name: String) = this.cursor.getLong(index(name))

    override fun double(name: String) = this.cursor.getDouble(index(name))

    override fun string(name: String) = this.cursor.getString(index(name))

    override fun has(name: String): Boolean {
        val idx = this.cursor.getColumnIndex(name)
        return idx > -1 && this.cursor.count > 0 && !this.cursor.isNull(idx)
    }

    private fun index(name: String): Int {
        val idx = this.cursor.getColumnIndex(name)
        if (idx == -1 || this.cursor.isNull(idx)) {
            throw Exception("Cursor does not contain column of $name name")
        }
        return idx
    }
}