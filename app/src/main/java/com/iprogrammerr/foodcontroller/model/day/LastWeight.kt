package com.iprogrammerr.foodcontroller.model.day

import com.iprogrammerr.foodcontroller.database.Database

class LastWeight(private val database: Database, private val default: Double) : Weight {

    override fun value(): Double {
        this.database.query("select weight from day order by id desc limit 1")
            .use { rs ->
                return if (rs.hasNext()) {
                    rs.next().double("weight")
                } else {
                    this.default
                }
            }
    }
}