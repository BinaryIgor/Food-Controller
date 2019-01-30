package com.iprogrammerr.foodcontroller.view.items

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.iprogrammerr.foodcontroller.R
import com.iprogrammerr.foodcontroller.model.Potential
import com.iprogrammerr.foodcontroller.model.meal.Meal
import com.iprogrammerr.foodcontroller.view.item.FoodView
import com.iprogrammerr.foodcontroller.view.item.SummaryView

class MealFoodView private constructor(
    private val meal: Meal,
    private val target: Potential<FoodWithActionTarget>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val food = 1
    private val summary = 2

    constructor(meal: Meal) : this(meal, Potential())

    constructor(meal: Meal, target: FoodWithActionTarget) : this(
        meal, Potential(target)
    )

    override fun onCreateViewHolder(group: ViewGroup,
        type: Int): RecyclerView.ViewHolder = when (type) {
        this.food -> FoodView(
            DataBindingUtil.inflate(
                LayoutInflater.from(group.context),
                R.layout.item_food,
                group,
                false
            ),
            this.target
        )
        this.summary -> SummaryView(
            DataBindingUtil.inflate(
                LayoutInflater.from(group.context),
                R.layout.item_summary,
                group,
                false
            )
        )
        else -> throw Exception("$type is not a valid type")
    }

    override fun getItemCount() = when {
        this.meal.food().size > 1 -> this.meal.food().size + 1
        else -> this.meal.food().size
    }

    override fun onBindViewHolder(view: RecyclerView.ViewHolder, position: Int) {
        if (view is FoodView) {
            view.draw(this.meal.food()[position])
        } else if (view is SummaryView) {
            view.draw(this.meal)
        }
    }

    override fun getItemViewType(position: Int) = when {
        position < this.meal.food().size -> this.food
        else -> this.summary
    }
}