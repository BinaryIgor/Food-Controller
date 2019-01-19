package com.iprogrammerr.foodcontroller.view.items

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.iprogrammerr.foodcontroller.R
import com.iprogrammerr.foodcontroller.view.item.ArrowedView
import java.util.*

class MonthsView(
    private val months: List<Calendar>,
    private val target: AdapterTarget<Calendar>
) : RecyclerView.Adapter<ArrowedView>(), PositionTarget {

    override fun onCreateViewHolder(group: ViewGroup, position: Int) =
        ArrowedView(
            DataBindingUtil.inflate(
                LayoutInflater.from(group.context), R.layout.item_arrowed, group, false
            ),
            this
        )

    override fun getItemCount() = this.months.size

    override fun onBindViewHolder(view: ArrowedView, position: Int) {
        view.draw(translatedMonth(view.itemView.context, this.months[position]))
    }

    private fun translatedMonth(context: Context, calendar: Calendar) =
        context.resources.getStringArray(R.array.months)[calendar[Calendar.MONTH]]


    override fun hit(position: Int) {
        this.target.hit(this.months[position])
    }
}