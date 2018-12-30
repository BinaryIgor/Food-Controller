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

class CategoryFoodView(
    products: List<FoodDefinition>,
    private val mainTarget: IdTarget,
    private val secondaryTarget: AdapterTarget<FoodDefinition>
) :
    RecyclerView.Adapter<ArrowedView>(), Refreshable<List<FoodDefinition>>, PositionTarget {

    private val products: MutableList<FoodDefinition> = ArrayList()
    private val moreTarget = object : PositionTarget {

        override fun hit(position: Int) {
            this@CategoryFoodView.secondaryTarget.hit(this@CategoryFoodView.products[position])
        }
    }

    init {
        this.products.addAll(products)
    }

    override fun onCreateViewHolder(group: ViewGroup, type: Int) = ArrowedView(
        DataBindingUtil.inflate(LayoutInflater.from(group.context), R.layout.item_arrowed, group, false),
        this, this.moreTarget
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
        this.mainTarget.hit(this.products[position].id())
    }
}