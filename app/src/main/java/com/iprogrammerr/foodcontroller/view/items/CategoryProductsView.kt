package com.iprogrammerr.foodcontroller.view.items

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.iprogrammerr.foodcontroller.R
import com.iprogrammerr.foodcontroller.model.IdTarget
import com.iprogrammerr.foodcontroller.model.food.FoodDefinition
import com.iprogrammerr.foodcontroller.view.Refreshable
import com.iprogrammerr.foodcontroller.view.item.ArrowedView

class CategoryProductsView(products: List<FoodDefinition>, private val target: IdTarget) :
    RecyclerView.Adapter<ArrowedView>(), Refreshable<List<FoodDefinition>>, PositionTarget {

    private val products: MutableList<FoodDefinition> = ArrayList()

    init {
        this.products.addAll(products)
    }

    override fun onCreateViewHolder(group: ViewGroup, type: Int) = ArrowedView(
        DataBindingUtil.inflate(LayoutInflater.from(group.context), R.layout.item_arrowed, group, false),
        this
    )

    override fun getItemCount() = this.products.size

    override fun onBindViewHolder(view: ArrowedView, position: Int) {
        view.draw(this.products[position].name())
    }

    override fun refresh(item: List<FoodDefinition>) {
        this.products.clear()
        this.products.addAll(item)
        notifyDataSetChanged()
    }

    override fun hit(position: Int) {
        this.target.hit(this.products[position].id())
    }
}