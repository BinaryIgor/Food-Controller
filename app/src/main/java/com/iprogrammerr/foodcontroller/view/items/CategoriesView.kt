package com.iprogrammerr.foodcontroller.view.items

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.iprogrammerr.foodcontroller.R
import com.iprogrammerr.foodcontroller.model.IdTarget
import com.iprogrammerr.foodcontroller.model.category.Category
import com.iprogrammerr.foodcontroller.view.item.ArrowedView

class CategoriesView(private val categories: List<Category>, private val target: IdTarget) :
    RecyclerView.Adapter<ArrowedView>(), PositionTarget {

    override fun onCreateViewHolder(group: ViewGroup, type: Int) =
        ArrowedView(
            DataBindingUtil.inflate(LayoutInflater.from(group.context), R.layout.item_arrowed, group, false),
            this
        )

    override fun getItemCount() = this.categories.size

    override fun onBindViewHolder(view: ArrowedView, position: Int) {
        view.draw(this.categories[position].name())
    }

    override fun hit(position: Int) {
        this.target.hit(this.categories[position].id())
    }
}