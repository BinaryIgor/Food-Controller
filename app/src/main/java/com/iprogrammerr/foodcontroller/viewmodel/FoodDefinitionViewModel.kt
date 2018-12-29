package com.iprogrammerr.foodcontroller.viewmodel

import android.arch.lifecycle.ViewModel
import com.iprogrammerr.foodcontroller.model.food.FoodDefinition
import com.iprogrammerr.foodcontroller.model.food.FoodDefinitions
import com.iprogrammerr.foodcontroller.model.result.Result
import com.iprogrammerr.foodcontroller.model.result.ResultValue
import java.util.concurrent.Executor

class FoodDefinitionViewModel(private val executor: Executor, private val definitions: FoodDefinitions) :
    ViewModel() {

    private lateinit var last: FoodDefinition

    fun update(
        id: Long, name: String, calories: Int, protein: Double,
        callback: (Result<Boolean>) -> Unit
    ) {
        this.executor.execute {
            try {
                this.definitions.definition(id).update(name, calories, protein)
                callback(ResultValue(true))
            } catch (e: Exception) {
                callback(ResultValue(e))
            }
        }
    }

    fun add(
        name: String, calories: Int, protein: Double, categoryId: Long,
        callback: (Result<Boolean>) -> Unit
    ) {
        this.executor.execute {
            try {
                this.definitions.create(name, calories, protein, categoryId)
                callback(ResultValue(true))
            } catch (e: Exception) {
                callback(ResultValue(e))
            }
        }
    }

    fun definition(id: Long, callback: (Result<FoodDefinition>) -> Unit) {
        this.executor.execute {
            if (this::last.isInitialized && this.last.id() == id) {
                callback(ResultValue(this.last))
            } else {
                try {
                    this.last = this.definitions.definition(id)
                    callback(ResultValue(this.last))
                } catch (e: Exception) {
                    callback(ResultValue(e))
                }
            }
        }
    }

    fun delete(id: Long, callback: (Result<Boolean>) -> Unit) {
        this.executor.execute {
            try {
                this.definitions.delete(id)
                callback(ResultValue(true))
            } catch (e: Exception) {
                callback(ResultValue(e))
            }
        }
    }
}
