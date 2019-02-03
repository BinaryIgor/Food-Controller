package com.iprogrammerr.foodcontroller.view

import android.content.Context
import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View
import kotlin.math.roundToInt

class TwoColumnsOffsetDecoration(
    context: Context,
    offset: Int
) : RecyclerView.ItemDecoration() {

    private val offset by lazy {
        (context.resources.displayMetrics.density * offset).roundToInt()
    }

    constructor(context: Context) : this(context, 4)

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView,
        state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        val pos = parent.getChildAdapterPosition(view)
        val top = if (pos < 2) 0 else this.offset * 2
        when (pos % 2) {
            1 -> outRect.set(this.offset, top, 0, 0)
            else -> outRect.set(0, top, this.offset, 0)
        }
    }
}