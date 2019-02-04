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

class FoodDefinitionsView(
    food: List<FoodDefinition>,
    private val mainTarget: IdTarget
) :
    RecyclerView.Adapter<ArrowedView>(), Refreshable<List<FoodDefinition>>, PositionTarget {

    private val products: MutableList<FoodDefinition> = ArrayList()
    private lateinit var moreTarget: PositionTarget

    constructor(
        food: List<FoodDefinition>,
        mainTarget: IdTarget,
        secondaryTarget: AdapterTarget<FoodDefinition>
    ) : this(
        food,
        mainTarget
    ) {
        this.moreTarget = object : PositionTarget {

            override fun hit(position: Int) {
                secondaryTarget.hit(this@FoodDefinitionsView.products[position])
            }
        }
    }

    init {
        this.products.addAll(food)
    }

    override fun onCreateViewHolder(group: ViewGroup, type: Int) =
        when {
            this::moreTarget.isInitialized -> ArrowedView(
                DataBindingUtil.inflate(
                    LayoutInflater.from(group.context),
                    R.layout.item_arrowed,
                    group,
                    false
                ),
                this,
                this.moreTarget
            )
            else -> ArrowedView(
                DataBindingUtil.inflate(
                    LayoutInflater.from(group.context),
                    R.layout.item_arrowed,
                    group,
                    false
                ),
                this
            )
        }

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