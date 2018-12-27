package com.iprogrammerr.foodcontroller.database.script

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import org.json.JSONArray
import org.json.JSONObject

class PrepopulatingScript(private val database: SQLiteDatabase, private val source: JSONObject) : Script {

    override fun execute() {
        insertCategories(this.source.getJSONArray("categories"))
        insertDefinitions(
            this.database.rawQuery("select id from category order by id asc", null).use { c ->
                val cs = ArrayList<Long>()
                while (c.moveToNext()) {
                    cs.add(c.getLong(0))
                }
                cs
            }
        )
    }

    private fun insertCategories(categories: JSONArray) {
        this.database.beginTransaction()
        for (i in 0 until categories.length()) {
            val values = ContentValues()
            values.put("name", categories.getString(i))
            this.database.insert("category", null, values)
        }
        this.database.setTransactionSuccessful()
        this.database.endTransaction()
    }

    private fun insertDefinitions(categories: List<Long>) {
        this.database.beginTransaction()
        for (i in categories.indices) {
            val definitions = this.source.getJSONArray("food_${i + 1}")
            for (j in 0 until definitions.length()) {
                val definition = definitions.getJSONObject(j)
                val values = ContentValues()
                values.put("name", definition.getString("name"))
                values.put("calories", definition.getInt("calories"))
                values.put("protein", definition.getDouble("protein"))
                values.put("category_id", categories[i])
                this.database.insert("food_definition", null, values)
            }
        }
        this.database.setTransactionSuccessful()
        this.database.endTransaction()
    }
}