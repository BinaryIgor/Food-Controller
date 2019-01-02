package com.iprogrammerr.foodcontroller.view.item

import android.support.v7.widget.RecyclerView
import com.iprogrammerr.foodcontroller.R
import com.iprogrammerr.foodcontroller.databinding.ItemFoodBinding
import com.iprogrammerr.foodcontroller.model.IdTarget
import com.iprogrammerr.foodcontroller.model.food.Food

//TODO more menu
class FoodView(private val binding: ItemFoodBinding, private val target: IdTarget) :
    RecyclerView.ViewHolder(binding.root),
    Drawable<Food> {

    override fun draw(item: Food) {
        this.binding.name.text = item.name()
        this.binding.weight.text = "${item.weight()} g"
        this.binding.values.text = String.format(
            this.binding.root.context.getString(R.string.values_template),
            item.calories(),
            item.protein()
        )
        this.binding.root.setOnClickListener { this.target.hit(item.id()) }
    }
}