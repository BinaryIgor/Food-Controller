package com.iprogrammerr.foodcontroller.database

import com.iprogrammerr.foodcontroller.model.Scalar
import com.iprogrammerr.foodcontroller.model.StickyScalar

class PortionContract private constructor(private val sql: Scalar<String>) : Contract {

    constructor() : this(
        StickyScalar {
            StringBuilder().append("CREATE TABLE portion ").append("(")
                .append("id INTEGER PRIMARY KEY AUTOINCREMENT").append(",")
                .append("food_id INTEGER NOT NULL")
                .append("weight INTEGER NOT NULL CHECK (weight > 0)")
                .append("UNIQUE (food_id, weight)")
                .append("FOREIGN KEY ")
                .append(")").toString()
        }
    )

    override fun createSql() = this.sql.value()
}