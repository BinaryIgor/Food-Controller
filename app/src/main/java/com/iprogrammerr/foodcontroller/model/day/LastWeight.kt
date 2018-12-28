package com.iprogrammerr.foodcontroller.model.day

import com.iprogrammerr.foodcontroller.database.Database

class LastWeight(private val database: Database, private val default: Double) : Weight {

    override fun value(): Double {
        this.database.query("select weight from day order by id desc limit 1")
            .use { rs ->
                val r = rs.next()
                return if (r.isEmpty()) {
                    this.default
                } else {
                    r.double("weight")
                }
            }
    }
}