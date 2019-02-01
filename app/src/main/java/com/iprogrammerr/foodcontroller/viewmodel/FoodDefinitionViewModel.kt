package com.iprogrammerr.foodcontroller.viewmodel

import android.arch.lifecycle.ViewModel
import com.iprogrammerr.foodcontroller.ObjectsPool
import com.iprogrammerr.foodcontroller.model.Asynchronous
import com.iprogrammerr.foodcontroller.model.category.Categories
import com.iprogrammerr.foodcontroller.model.category.Category
import com.iprogrammerr.foodcontroller.model.food.FoodDefinition
import com.iprogrammerr.foodcontroller.model.food.FoodDefinitions
import com.iprogrammerr.foodcontroller.model.format.Formats
import com.iprogrammerr.foodcontroller.model.result.Callback

class FoodDefinitionViewModel(
    private val asynchronous: Asynchronous,
    private val definitions: FoodDefinitions,
    categories: Categories,
    val formats: Formats
) : ViewModel() {

    private lateinit var last: FoodDefinition
    private val categories by lazy {
        categories.all()
    }

    constructor() : this(
        ObjectsPool.single(Asynchronous::class.java),
        ObjectsPool.single(FoodDefinitions::class.java),
        ObjectsPool.single(Categories::class.java),
        ObjectsPool.single(Formats::class.java)
    )

    fun update(id: Long, name: String, calories: Int, protein: Double,
        callback: Callback<Boolean>) {
        this.asynchronous.execute({
            this.definitions.definition(id).update(name, calories, protein)
            true
        }, callback)
    }

    fun add(name: String, calories: Int, protein: Double, categoryId: Long,
        callback: Callback<Boolean>) {
        this.asynchronous.execute({
            this.definitions.create(name, calories, protein, categoryId)
            true
        }, callback)
    }

    fun definition(id: Long, callback: Callback<FoodDefinition>) {
        this.asynchronous.execute({
            if (!this::last.isInitialized || this.last.id() != id) {
                this.last = this.definitions.definition(id)
            }
            this.last
        }, callback)
    }

    fun delete(id: Long, callback: Callback<Boolean>) {
        this.asynchronous.execute({
            this.definitions.delete(id)
            true
        }, callback)
    }

    fun categories(callback: Callback<List<Category>>) {
        this.asynchronous.execute({ this.categories }, callback)
    }
}
