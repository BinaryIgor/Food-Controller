package com.iprogrammerr.foodcontroller.model.day

import com.iprogrammerr.foodcontroller.database.Database

class LastWeight(private val database: Database, private val default: Double) : Weight {

    override fun value(): Double {
        this.database.query("SELECT weight FROM day ORDER BY id DESC LIMIT 1")
            .use { rs ->
                val r = rs.next()
                return if (r.has("weight")) {
                    r.double("weight")
                } else {
                    this.default
                }
            }
    }
}