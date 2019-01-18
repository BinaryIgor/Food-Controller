package com.iprogrammerr.foodcontroller.view

import android.support.v7.widget.PopupMenu
import android.view.ContextThemeWrapper
import android.view.Gravity
import android.view.View
import com.iprogrammerr.foodcontroller.R

class DeletePopup(
    private val anchor: View,
    private val delete: () -> Unit
) : Component {

    override fun show() {
        val popup = PopupMenu(
            ContextThemeWrapper(this.anchor.context, R.style.PopUpStyle), this.anchor
        )
        popup.gravity = Gravity.END
        popup.menuInflater.inflate(R.menu.delete_drop_down, popup.menu)
        popup.setOnMenuItemClickListener {
            this.delete()
            true
        }
        popup.show()
    }
}