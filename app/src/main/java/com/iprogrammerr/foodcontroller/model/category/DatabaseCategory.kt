package com.iprogrammerr.foodcontroller.model.category

import android.content.ContentValues
import com.iprogrammerr.foodcontroller.database.Database
import com.iprogrammerr.foodcontroller.model.food.DatabaseFoodDefinition
import com.iprogrammerr.foodcontroller.model.food.FoodDefinition
import java.util.ArrayList
import kotlin.collections.HashMap
import kotlin.collections.set

class DatabaseCategory(private val id: Long, private val database: Database) : Category {

    constructor(id: Long, name: String, database: Database) : this(id, database) {
        this.fields["name"] = name
    }

    private val fields = HashMap<String, Any>()

    override fun id() = this.id

    override fun name(): String {
        if (!this.fields.containsKey("name")) {
            this.database.query("select name from category where id = ${this.id}").use { r ->
                this.fields["name"] = r.next().string("name")
            }
        }
        return this.fields["name"] as String
    }

    override fun rename(name: String) {
        val values = ContentValues()
        values.put("name", name)
        this.database.update("category", "id = ${this.id}", values)
        this.fields.remove("name")
    }

    override fun food(): List<FoodDefinition> {
        return this.database.query("select * from food_definition where category_id = ${this.id}")
            .use { rs ->
                val products: MutableList<FoodDefinition> = ArrayList()
                while (rs.hasNext()) {
                    val r = rs.next()
                    products.add(
                        DatabaseFoodDefinition(
                            r.long("id"), this.database,
                            r.string("name"), r.int("calories"),
                            r.double("protein"), r.long("category_id")
                        )
                    )
                }
                products
            }
    }

    override fun addFood(id: Long) {
        val values = ContentValues()
        values.put("category_id", this.id)
        this.database.update("food_definition", "id = $id", values)
    }
}