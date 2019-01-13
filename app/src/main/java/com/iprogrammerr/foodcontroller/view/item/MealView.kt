package com.iprogrammerr.foodcontroller.view.item

import android.support.v7.widget.RecyclerView
import com.iprogrammerr.foodcontroller.R
import com.iprogrammerr.foodcontroller.databinding.ItemMealBinding
import com.iprogrammerr.foodcontroller.model.meal.Meal
import com.iprogrammerr.foodcontroller.view.EditDeletePopup
import com.iprogrammerr.foodcontroller.view.items.IdWithActionTarget
import java.util.*

class MealView(private val binding: ItemMealBinding, private val target: IdWithActionTarget) :
    RecyclerView.ViewHolder(binding.root), Drawable<Meal> {

    override fun draw(item: Meal) {
        val context = this.binding.root.context
        this.binding.title.text = String.format(context.getString(R.string.meal_number),
            this.adapterPosition + 1)
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = item.time()
        this.binding.hour.text = String.format(
            context.getString(R.string.time_template),
            calendar[Calendar.HOUR_OF_DAY], calendar[Calendar.MINUTE]
        )
        this.binding.root.setOnClickListener {
            EditDeletePopup(
                this.binding.root,
                { this.target.hit(item.id(), IdWithActionTarget.Action.EDIT) },
                { this.target.hit(item.id(), IdWithActionTarget.Action.DELETE) }
            ).show()
        }
    }
}