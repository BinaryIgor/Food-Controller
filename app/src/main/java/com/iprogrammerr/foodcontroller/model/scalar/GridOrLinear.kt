package com.iprogrammerr.foodcontroller.model.scalar

import android.content.Context
import android.content.res.Configuration
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.iprogrammerr.foodcontroller.view.LinearOffsetDecoration
import com.iprogrammerr.foodcontroller.view.TwoColumnsOffsetDecoration

class GridOrLinear(
    private val context: Context,
    private val view: RecyclerView
) : Scalar<RecyclerView.LayoutManager> {

    override fun value() =
        if (this.context.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            this.view.addItemDecoration(LinearOffsetDecoration(this.context))
            LinearLayoutManager(this.context)
        } else {
            this.view.addItemDecoration(TwoColumnsOffsetDecoration(this.context))
            GridLayoutManager(this.context, 2)
        }
}