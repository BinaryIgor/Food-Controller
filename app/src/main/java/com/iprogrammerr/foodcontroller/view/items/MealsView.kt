package com.iprogrammerr.foodcontroller.view.items

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.iprogrammerr.foodcontroller.R
import com.iprogrammerr.foodcontroller.model.IdTarget
import com.iprogrammerr.foodcontroller.model.meal.Meal
import com.iprogrammerr.foodcontroller.view.item.MealView

class MealsView(private val meals: List<Meal>, private val target: IdTarget) : RecyclerView.Adapter<MealView>() {

    override fun onCreateViewHolder(group: ViewGroup, position: Int) = MealView(
        DataBindingUtil.inflate(LayoutInflater.from(group.context), R.layout.item_meal, group, false),
        this.target
    )

    override fun getItemCount() = this.meals.size

    override fun onBindViewHolder(view: MealView, position: Int) {
        view.draw(this.meals[position])
    }
}