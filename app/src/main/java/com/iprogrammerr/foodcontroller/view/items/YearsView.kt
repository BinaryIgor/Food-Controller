package com.iprogrammerr.foodcontroller.view.items

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.iprogrammerr.foodcontroller.R
import com.iprogrammerr.foodcontroller.view.item.ArrowedView

class YearsView(
    private val years: List<Int>,
    private val target: AdapterTarget<Int>
) : RecyclerView.Adapter<ArrowedView>(), PositionTarget {

    override fun onCreateViewHolder(group: ViewGroup, position: Int) =
        ArrowedView(
            DataBindingUtil.inflate(
                LayoutInflater.from(group.context),
                R.layout.item_arrowed,
                group,
                false
            ),
            this
        )

    override fun getItemCount() = this.years.size

    override fun onBindViewHolder(view: ArrowedView, position: Int) {
        view.draw(this.years[position].toString())
    }

    override fun hit(position: Int) {
        this.target.hit(this.years[position])
    }
}