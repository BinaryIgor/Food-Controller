package com.iprogrammerr.foodcontroller.view.items

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.iprogrammerr.foodcontroller.R
import com.iprogrammerr.foodcontroller.model.day.Day
import com.iprogrammerr.foodcontroller.view.item.DayView
import java.text.DateFormat

class DaysView(
    private val format: DateFormat,
    private val days: List<Day>,
    private val target: DateTarget
) : RecyclerView.Adapter<DayView>() {

    override fun onCreateViewHolder(group: ViewGroup, position: Int) =
        DayView(
            DataBindingUtil.inflate(
                LayoutInflater.from(group.context), R.layout.item_day, group, false
            ),
            this.format,
            this.target
        )

    override fun getItemCount() = this.days.size

    override fun onBindViewHolder(view: DayView, position: Int) {
        view.draw(this.days[position])
    }
}