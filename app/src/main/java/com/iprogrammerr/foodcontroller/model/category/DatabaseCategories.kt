package com.iprogrammerr.foodcontroller.model.category

import com.iprogrammerr.foodcontroller.database.Database

class DatabaseCategories(private val database: Database) : Categories {

    override fun value(): List<Category> {
        this.database.query("SELECT * FROM category").use { rs ->
            val categories = ArrayList<Category>()
            while (rs.hasNext()) {
                val r = rs.next()
                categories.add(DatabaseCategory(r.long("id"), r.string("name"), this.database))
            }
            return categories
        }
    }
}