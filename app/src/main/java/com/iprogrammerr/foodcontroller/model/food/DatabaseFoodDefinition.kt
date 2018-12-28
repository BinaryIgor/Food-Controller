package com.iprogrammerr.foodcontroller.model.food

import com.iprogrammerr.foodcontroller.database.Database

class DatabaseFoodDefinition(private val id: Long, private val database: Database) :
    FoodDefinition {

    constructor(id: Long, database: Database, name: String, calories: Int, protein: Double, categoryId: Long) :
            this(id, database) {
        this.attributes["name"] = name
        this.attributes["calories"] = calories
        this.attributes["protein"] = protein
        this.attributes["categoryId"] = categoryId
    }

    private val attributes: MutableMap<String, Any> = HashMap()

    override fun id() = this.id

    override fun name(): String {
        return loadedOrCached("name")
    }

    override fun calories(): Int {
        return loadedOrCached("calories")
    }

    override fun protein(): Double {
        return loadedOrCached("protein")
    }

    override fun update(values: Map<String, Any>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun load() {
        this.database.query("select * from food_definition where id = ${this.id}")
            .use { rs ->
                val r = rs.next()
                this.attributes["name"] = r.string("name")
                this.attributes["weight"] = r.int("weight")
                this.attributes["protein"] = r.double("protein")
                this.attributes["categoryId"] = r.long("category_id")
            }
    }

    private inline fun <reified T> loadedOrCached(key: String): T {
        if (this.attributes.isEmpty()) {
            load()
        }
        return this.attributes[key] as T
    }
}