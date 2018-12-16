package com.iprogrammerr.foodcontroller.database

import com.iprogrammerr.foodcontroller.model.Scalar
import com.iprogrammerr.foodcontroller.model.StickyScalar

class FoodContract private constructor(private val sql: Scalar<String>) : Contract {

    constructor() : this(
        StickyScalar {
            StringBuilder().append("CREATE TABLE food ").append("(")
                .append("id INTEGER PRIMARY KEY AUTOINCREMENT").append(",")
                .append("name TEXT NOT NULL UNIQUE").append(",")
                .append("calories INTEGER NOT NULL CHECK (calories > 0)").append(",")
                .append("protein INTEGER NOT NULL CHECK (protein >= 0)").append(",")
                .append("deleted INTEGER DEFAULT 0 CHECK (deleted >= 0 && deleted <= 1)")
                .append(")").toString()
        }
    )

    override fun createSql() = this.sql.value()
}