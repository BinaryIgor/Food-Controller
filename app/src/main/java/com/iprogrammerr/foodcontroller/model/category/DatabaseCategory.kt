package com.iprogrammerr.foodcontroller.model.category

import android.content.ContentValues
import com.iprogrammerr.foodcontroller.database.Database
import com.iprogrammerr.foodcontroller.model.food.FoodDefinition

class DatabaseCategory(private val id: Long, private val database: Database) : Category {

    private val fields = HashMap<String, Any>()

    override fun name(): String {
        if (!this.fields.containsKey("name")) {
            val cursor = this.database.query("select name from category where id = $this.id")
            cursor.moveToNext()
            this.fields["name"] = cursor.getString(0)
            cursor.close()
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