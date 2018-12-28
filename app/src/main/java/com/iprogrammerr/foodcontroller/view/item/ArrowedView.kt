package com.iprogrammerr.foodcontroller.view.item

import android.support.v7.widget.RecyclerView
import com.iprogrammerr.foodcontroller.databinding.ItemArrowedBinding
import com.iprogrammerr.foodcontroller.view.items.PositionTarget

class ArrowedView(private val binding: ItemArrowedBinding, private val target: PositionTarget) :
    RecyclerView.ViewHolder(binding.root), Drawable<String> {

    override fun draw(item: String) {
        this.binding.title.text = item
        this.binding.root.setOnClickListener { this.target.hit(adapterPosition) }
    }
}