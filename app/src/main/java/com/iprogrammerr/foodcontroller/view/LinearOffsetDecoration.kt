package com.iprogrammerr.foodcontroller.view

import android.content.Context
import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View
import kotlin.math.roundToInt

class LinearOffsetDecoration(
    context: Context,
    offset: Int
) : RecyclerView.ItemDecoration() {

    private val offset by lazy {
        (context.resources.displayMetrics.density * offset).roundToInt()
    }

    constructor(context: Context) : this(context, 8)

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView,
        state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        when (parent.getChildAdapterPosition(view)) {
            0 -> outRect.set(0, 0, 0, 0)
            else -> outRect.set(0, this.offset, 0, 0)
        }
    }
}