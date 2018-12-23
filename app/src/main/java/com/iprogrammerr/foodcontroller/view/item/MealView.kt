package com.iprogrammerr.foodcontroller.view.item

import android.support.v7.widget.RecyclerView
import com.iprogrammerr.foodcontroller.R
import com.iprogrammerr.foodcontroller.databinding.ItemMealBinding
import com.iprogrammerr.foodcontroller.model.IdTarget
import com.iprogrammerr.foodcontroller.model.meal.Meal
import java.util.*

//TODO more menu
class MealView(private val binding: ItemMealBinding, private val target: IdTarget) :
    RecyclerView.ViewHolder(binding.root), Drawable<Meal> {

    override fun draw(item: Meal) {
        val context = this.binding.root.context
        this.binding.title.text = String.format(context.getString(R.string.meal_number), this.adapterPosition + 1)
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = item.time()
        this.binding.hour.text = String.format(
            context.getString(R.string.time_template),
            calendar[Calendar.HOUR_OF_DAY], calendar[Calendar.MINUTE]
        )
        this.binding.root.setOnClickListener { this.target.hit(item.id()) }
    }
}