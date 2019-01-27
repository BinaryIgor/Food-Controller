package com.iprogrammerr.foodcontroller.model.food

import android.content.ContentValues
import com.iprogrammerr.foodcontroller.database.Database
import com.iprogrammerr.foodcontroller.database.Row

class DatabaseFoodDefinitions(private val database: Database) : FoodDefinitions {

    override fun create(name: String, calories: Int, protein: Double, categoryId: Long) {
        val values = ContentValues()
        values.put("name", name)
        values.put("calories", calories)
        values.put("protein", protein)
        values.put("category_id", categoryId)
        this.database.insert("food_definition", values)
    }

    override fun all(): List<FoodDefinition> {
        return this.database.query("SELECT * from food_definition ORDER BY name ASC")
            .use { rs ->
                val food: MutableList<FoodDefinition> = ArrayList()
                while (rs.hasNext()) {
                    food.add(definition(rs.next()))
                }
                food
            }
    }

    private fun definition(row: Row) = DatabaseFoodDefinition(
        row.long("id"),
        this.database,
        row.string("name"),
        row.int("calories"),
        row.double("protein")
    )

    override fun definition(id: Long): FoodDefinition {
        return this.database.query("SELECT * from food_definition WHERE id = $id")
            .use { rs ->
                definition(rs.next())
            }
    }

    override fun delete(id: Long) {
        val usage = this.database.query(
            StringBuilder("SELECT COUNT(*) c from food_definition fd ")
                .append("INNER JOIN food f on fd.id = f.definition_id ")
                .append("INNER JOIN food_meal fm on f.id = fm.food_id ")
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