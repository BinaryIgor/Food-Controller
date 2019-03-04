package com.iprogrammerr.foodcontroller.model.meal

import android.content.ContentValues
import com.iprogrammerr.foodcontroller.database.Database
import com.iprogrammerr.foodcontroller.database.Rows
import com.iprogrammerr.foodcontroller.model.food.ConstantFood
import com.iprogrammerr.foodcontroller.model.food.Food
import java.text.SimpleDateFormat
import java.util.*

class DatabaseMeals(private val database: Database) : Meals {

    override fun last(limit: Int) = this.database.query(lastMealsQuery(2 * limit)).use { rs ->
        val uniques = HashSet<String>(limit)
        val last = ArrayList<Meal>(limit)
        if (rs.next().has("m_id")) {
            do {
                val meal = meal(rs)
                val hash = uniqueHash(meal)
                if (!uniques.contains(hash)) {
                    uniques.add(hash)
                    last.add(meal)
                }
            } while (last.size < limit && rs.current().long("m_id") != meal.id())
        }
        last
    }

    private fun lastMealsQuery(limit: Int) =
        StringBuilder(
            "SELECT m.id as m_id, time, f.id as f_id, f.weight, fd.id as fd_id, fd.name, fd.protein, fd.calories "
        )
            .append("FROM meal m INNER JOIN food_meal fm ON m.id = fm.meal_id ")
            .append("INNER JOIN food f ON fm.food_id = f.id ")
            .append("INNER JOIN food_definition fd ON f.definition_id = fd.id ")
            .append("WHERE m.id IN (SELECT id FROM meal ORDER BY time DESC LIMIT $limit) ")
            .append("ORDER BY time DESC")
            .toString()


    private fun uniqueHash(meal: Meal): String {
        val args = meal.food().sortedBy { f -> f.id() }
        val builder = StringBuilder()
        for (f in args) {
            builder.append(f.id()).append("|")
        }
        return builder.toString()
    }

    override fun meal(id: Long) =
        this.database.query(
            StringBuilder(
                "SELECT m.id as m_id, time, f.id as f_id, f.weight, fd.id as fd_id, fd.name, fd.protein, fd.calories "
            )
                .append("FROM meal m LEFT JOIN food_meal fm ON m.id = fm.meal_id ")
                .append("LEFT JOIN food f ON fm.food_id = f.id ")
                .append("LEFT JOIN food_definition fd ON f.definition_id = fd.id ")
                .append("WHERE m.id = $id")
                .toString()
        ).use { rs ->
            rs.next()
            meal(rs)
        }

    private fun meal(rows: Rows): Meal {
        val food: MutableList<Food> = ArrayList()
        var r = rows.current()
        val id = r.long("m_id")
        val time = r.long("time")
        if (r.has("f_id")) {
            do {
                food.add(
                    ConstantFood(
                        r.long("f_id"),
                        r.string("name"),
                        r.int("weight"),
                        r.int("calories"),
                        r.double("protein"),
                        r.long("fd_id")
                    )
                )
                if (!rows.hasNext()) {
                    break
                }
                r = rows.next()
            } while (id == r.long("m_id"))
        }
        return DatabaseMeal(id, this.database, time, food)
    }

    override fun create(time: Long, dayId: Long) =
        this.database.query(
            StringBuilder("SELECT m.id as m_id, COUNT(fm.id) food FROM meal m ")
                .append("LEFT JOIN food_meal fm ON m.id = fm.meal_id ")
                .append("GROUP BY m_id HAVING food == 0 ")
                .append("ORDER BY time DESC LIMIT 1")
                .toString()
        ).use { rs ->
            val values = ContentValues()
            values.put("time", time)
            values.put("day_id", dayId)
            val r = rs.next()
            if (r.has("m_id")) {
                val id = r.long("m_id")
                this.database.update("meal", "id = $id", values)
                id
            } else {
                this.database.insert("meal", values)
            }
        }

    override fun clone(id: Long, meal: Meal) {
        this.database.delete("food_meal", "meal_id = $id")
        for (f in meal.food()) {
            val values = ContentValues()
            values.put("food_id", f.id())
            values.put("meal_id", id)
            this.database.insert("food_meal", values)
        }
    }

    override fun delete(id: Long) {
        this.database.delete("meal", "id = $id")
    }
}