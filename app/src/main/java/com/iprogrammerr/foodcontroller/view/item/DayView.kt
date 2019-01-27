package com.iprogrammerr.foodcontroller.view.item

import android.support.v7.widget.RecyclerView
import com.iprogrammerr.foodcontroller.databinding.ItemDayBinding
import com.iprogrammerr.foodcontroller.model.IdTarget
import com.iprogrammerr.foodcontroller.model.NutritionalValues
import com.iprogrammerr.foodcontroller.model.day.Day
import java.text.DateFormat
import kotlin.math.roundToInt

class DayView(
    private val binding: ItemDayBinding,
    private val format: DateFormat,
    private val target: IdTarget
) : RecyclerView.ViewHolder(binding.root), Drawable<Day> {

    override fun draw(item: Day) {
        this.binding.title.text = this.format.format(item.date())
        val goals = item.goals()
        val values = dayValues(item)
        this.binding.caloriesProgress.max = goals.calories()
        this.binding.caloriesProgress.progress = values.calories()
        this.binding.proteinProgress.max = goals.protein().roundToInt()
        this.binding.proteinProgress.progress = values.protein().roundToInt()
        this.binding.root.setOnClickListener { this.target.hit(item.id()) }
    }

    private fun dayValues(day: Day): NutritionalValues {
        var calories = 0
        var protein = 0.0
        for (m in day.meals()) {
            val values = m.nutritionalValues()
            calories += values.calories()
            protein += values.protein()
        }
        return object : NutritionalValues {

            override fun calories() = calories

            override fun protein() = protein
        }
    }
}