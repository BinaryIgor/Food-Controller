package com.iprogrammerr.foodcontroller.database.contract

//TODO create database architecture by creating Contracts implementations
interface Contract {
    fun createSql() : String
}