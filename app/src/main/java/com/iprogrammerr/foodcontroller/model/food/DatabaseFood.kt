package com.iprogrammerr.foodcontroller.model.food

import com.iprogrammerr.foodcontroller.database.Database
import kotlin.math.roundToInt

class DatabaseFood(
    private val id: Long,
    private val database: Database,
    private val attributes: MutableMap<String, Any>
) : Food {

    constructor(id: Long, database: Database) : this(id, database, HashMap())

    override fun id() = this.id

    override fun name(): String {
        if (!this.attributes.contains("name")) {
            load()
        }
        return this.attributes["name"] as String
    }

    override fun weight(): Int {
        if (!this.attributes.contains("weight")) {
            load()
        }
        return this.attributes["weight"] as Int
    }

    override fun calories(): Int {
        if (!this.attributes.contains("calories")) {
            load()
        }
        return ((weight() / 100.0) * this.attributes["calories"] as Int).roundToInt()
    }

    override fun protein(): Int {
        if (!this.attributes.contains("protein")) {
            load()
        }
        return ((weight() / 100.0) * this.attributes["protein"] as Int).roundToInt()
    }

    private fun load() {
        val cursor = this.database.query(
            "select * fom food inner join food_definition on food.definition_id = food_definition.id where id = ${this.id}"
        )
        cursor.moveToNext()
        this.attributes["name"] = cursor.getLong(cursor.getColumnIndex("name"))
        this.attributes["weight"] = cursor.getInt(cursor.getColumnIndex("weight"))
        this.attributes["calories"] = cursor.getInt(cursor.getColumnIndex("calories"))
        this.attributes["protein"] = cursor.getInt(cursor.getColumnIndex("protein"))
        cursor.close()
    }
}