package com.iprogrammerr.foodcontroller.database

import com.iprogrammerr.foodcontroller.model.Scalar
import com.iprogrammerr.foodcontroller.model.StickyScalar

class FoodPortionContract private constructor(private val sql: Scalar<String>) : Contract {

    constructor() : this(
        StickyScalar {
            StringBuilder().append("CREATE TABLE food_portion ").append("(")
                .append("id INTEGER PRIMARY KEY AUTOINCREMENT").append(",")
                .append("food_id INTEGER NOT NULL").append(",")
                .append("portion_id INTEGER NOT NULL").append(",")
                .append("FOREIGN KEY ")
                .append(")").toString()
        }
    )

    override fun createSql() = this.sql.value()
}