package com.iprogrammerr.foodcontroller.view.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import com.iprogrammerr.foodcontroller.R
import com.iprogrammerr.foodcontroller.databinding.DialogTwoOptionsBinding

class TwoOptionsDialog : DialogFragment() {

    private lateinit var target: Target

    companion object {
        fun new(title: String, description: String, left: String, right: String): TwoOptionsDialog {
            val dialog = TwoOptionsDialog()
            val args = Bundle()
            args.putString("title", title)
            args.putString("description", description)
            args.putString("left", left)
            args.putString("right", right)
            dialog.arguments = args
            return dialog
        }

        fun new(description: String, left: String, right: String) =
            new(description, "", left, right)

        fun tag() = this::class.java.simpleName as String
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (this.parentFragment == null) {
            this.target = context as Target
        } else {
            this.target = this.parentFragment as Target
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = AlertDialog.Builder(this.context).create()
        val binding: DialogTwoOptionsBinding = DataBindingUtil.inflate(
            LayoutInflater.from(this.context), R.layout.dialog_two_options, null, false
        )
        val arguments = this.arguments as Bundle
        binding.title.text = arguments.getString("title", "")
        val description = arguments.getString("description", "")
        if (description.isEmpty()) {
            binding.description.visibility = View.GONE
        } else {
            binding.description.text = description
        }
        binding.left.text = arguments.getString("left", "")
        binding.left.setOnClickListener {
            dialog.dismiss()
            this.target.hitLeft()

        }
        binding.right.text = arguments.getString("right", "")
        binding.right.setOnClickListener {
            dialog.dismiss()
            this.target.hitRight()
        }
        dialog.setView(binding.root)
        return dialog
    }

    fun show(manager: FragmentManager) {
        if (manager.findFragmentByTag(tag()) == null) {
            show(manager, tag())
        }
    }

    interface Target {

        fun hitLeft()

        fun hitRight()
    }
}