package com.iprogrammerr.foodcontroller.view.item

import android.support.v7.widget.RecyclerView
import com.iprogrammerr.foodcontroller.databinding.ItemArrowedBinding
import com.iprogrammerr.foodcontroller.view.items.PositionTarget

class ArrowedView(private val binding: ItemArrowedBinding, private val rootTarget: PositionTarget) :
    RecyclerView.ViewHolder(binding.root), Drawable<String> {

    private lateinit var arrowTarget: PositionTarget

    constructor(binding: ItemArrowedBinding, rootTarget: PositionTarget, arrowTarget: PositionTarget)
            : this(binding, rootTarget) {
        this.arrowTarget = arrowTarget
    }

    override fun draw(item: String) {
        this.binding.title.text = item
        this.binding.root.setOnClickListener { this.rootTarget.hit(this.adapterPosition) }
        if (this::arrowTarget.isInitialized) {
            this.binding.arrow.setOnClickListener { this.arrowTarget.hit(this.adapterPosition) }
        }
    }
}