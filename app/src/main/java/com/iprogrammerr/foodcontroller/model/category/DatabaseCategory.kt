package com.iprogrammerr.foodcontroller.model.category

import android.content.ContentValues
import com.iprogrammerr.foodcontroller.database.Database
import com.iprogrammerr.foodcontroller.model.food.FoodDefinition

class DatabaseCategory(private val id: Long, private val database: Database) : Category {

    constructor(id: Long, name: String, database: Database) : this(id, database) {
        this.fields["name"] = name
    }

    private val fields = HashMap<String, Any>()

    override fun name(): String {
        if (!this.fields.containsKey("name")) {
            this.database.query("select name from category where id = $this.id").use { r ->
                this.fields["name"] = r.next().string("name")
            }
        }
        return this.fields["name"] as String
    }

    override fun rename(name: String) {
        val values = ContentValues()
        values.put("name", name)
        this.database.update("category", "id = $this.id", values)
        this.fields.remove("name")
    }

    override fun food(): List<FoodDefinition> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}