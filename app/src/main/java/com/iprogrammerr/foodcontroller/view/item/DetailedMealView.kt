package com.iprogrammerr.foodcontroller.view.item

import android.support.v7.widget.RecyclerView
import com.iprogrammerr.foodcontroller.R
import com.iprogrammerr.foodcontroller.databinding.ItemDetailedMealBinding
import com.iprogrammerr.foodcontroller.model.meal.Meal
import com.iprogrammerr.foodcontroller.view.items.AdapterTarget
import kotlin.math.roundToInt

class DetailedMealView(
    private val binding: ItemDetailedMealBinding,
    private val target: AdapterTarget<Meal>
) : RecyclerView.ViewHolder(binding.root), Drawable<Meal> {

    override fun draw(item: Meal) {
        val values = item.nutritionalValues()
        this.binding.title.text = String.format(
            this.binding.root.context.getString(R.string.values_template),
            values.calories(),
            values.protein().roundToInt()
        )
        if (item.food().isNotEmpty()) {
            val food = item.food()
            val builder = StringBuilder()
            builder.append("${food[0].name()} ${food[0].weight()} g")
            for (i in 1 until food.size) {
                builder.append(System.lineSeparator())
                    .append("${food[i].name()} ${food[i].weight()} g")
            }
            this.binding.food.text = builder.toString()
        }
        this.binding.root.setOnClickListener { this.target.hit(item) }
    }
}