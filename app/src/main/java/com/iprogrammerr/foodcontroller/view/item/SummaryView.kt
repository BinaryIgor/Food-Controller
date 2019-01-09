package com.iprogrammerr.foodcontroller.view.item

import android.support.v7.widget.RecyclerView
import com.iprogrammerr.foodcontroller.R
import com.iprogrammerr.foodcontroller.databinding.ItemSummaryBinding
import com.iprogrammerr.foodcontroller.model.meal.Meal
import kotlin.math.roundToInt

class SummaryView(private val binding: ItemSummaryBinding) :
    RecyclerView.ViewHolder(binding.root),
    Drawable<Meal> {

    override fun draw(item: Meal) {
        val values = item.nutritionalValues()
        this.binding.values.text = String.format(
            this.binding.root.context.getString(R.string.values_template),
            values.calories(),
            values.protein().roundToInt()
        )
    }
}