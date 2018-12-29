package com.iprogrammerr.foodcontroller.model.food

import android.content.ContentValues
import com.iprogrammerr.foodcontroller.database.Database

class DatabaseFoodDefinitions(private val database: Database) : FoodDefinitions {

    override fun create(name: String, calories: Int, protein: Double, categoryId: Long) {
        val values = ContentValues()
        values.put("name", name)
        values.put("calories", calories)
        values.put("protein", protein)
        values.put("category_id", categoryId)
        this.database.insert("food_definition", values)
    }

    override fun definition(id: Long): FoodDefinition {
        return this.database.query("SELECT * from food_definition WHERE id = $id")
            .use { rs ->
                val r = rs.next()
                DatabaseFoodDefinition(
                    r.long("id"), this.database, r.string("name"),
                    r.int("calories"), r.double("protein"), r.long("category_id")
                )
            }
    }

    override fun delete(id: Long) {
        val usage = this.database.query(
            StringBuilder()
                .append("SELECT COUNT(*) c from food_definition fd ")
                .append("INNER JOIN food f on fd.id = f.definition_id ")
                .append("INNER JOIN food_meal fm on f.id = fm.food_id")
                .append("WHERE fd.id = $id")
                .toString()
        ).use { rs -> rs.next().int("c") }
        if (usage > 0) {
            val values = ContentValues()
            values.put("deleted", 1)
            this.database.update("food_definition", "id = $id", values)
        } else {
            this.database.delete("food_definition", "id = $id")
        }
    }
}