package com.iprogrammerr.foodcontroller.database

//TODO create database architecture by creating Contracts implementations
interface Contract {
    fun createSql() : String
}