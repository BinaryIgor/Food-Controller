package com.iprogrammerr.foodcontroller.database

import android.database.Cursor

class DatabaseRows(private val cursor: Cursor) : Rows {

    override fun next(): Row {
        this.cursor.moveToNext()
        return DatabaseRow(this.cursor)
    }

    override fun hasNext() = !this.cursor.isAfterLast

    override fun close() {
        this.cursor.close()
    }
}