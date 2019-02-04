package com.iprogrammerr.foodcontroller.view.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import com.iprogrammerr.foodcontroller.R
import com.iprogrammerr.foodcontroller.databinding.DialogInformationBinding

class InformationDialog : DialogFragment() {

    companion object {

        private const val TITLE = "TITLE"
        private const val DESCRIPTION = "DESCRIPTION"

        fun new(title: String, description: String): InformationDialog {
            val dialog = InformationDialog()
            val args = Bundle()
            args.putString(TITLE, title)
            args.putString(DESCRIPTION, description)
            dialog.arguments = args
            return dialog
        }

        fun new(description: String) = new(description, "")

        fun tag() = this::class.java.simpleName as String
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = AlertDialog.Builder(this.context).create()
        val binding: DialogInformationBinding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.dialog_information,
            null,
            false
        )
        val args = this.arguments as Bundle
        binding.title.text = args.getString(TITLE, "")
        val description = args.getString(DESCRIPTION, "")
        if (description.isEmpty()) {
            binding.description.visibility = View.GONE
        } else {
            binding.description.text = description
        }
        binding.ok.setOnClickListener { dialog.dismiss() }
        dialog.setView(binding.root)
        return dialog
    }

    fun show(manager: FragmentManager) {
        if (manager.findFragmentByTag(tag()) == null) {
            show(manager, tag())
        }
    }
}