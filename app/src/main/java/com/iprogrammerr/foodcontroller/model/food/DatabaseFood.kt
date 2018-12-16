package com.iprogrammerr.foodcontroller.model.food

import com.iprogrammerr.foodcontroller.database.Database

class DatabaseFood(private val id: Long, private var weight: Int, private val database: Database) : Food {

    override fun id(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun name(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun weight(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateWeight(weight: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun calories(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun protein(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}