package com.iprogrammerr.foodcontroller.view.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.view.LayoutInflater
import com.iprogrammerr.foodcontroller.R
import com.iprogrammerr.foodcontroller.databinding.DialogErrorBinding

class ErrorDialog : DialogFragment() {

    private val description by lazy {
        this.arguments?.let { args -> args.getString("description", "") } ?: ""
    }

    companion object {
        fun new(description: String): ErrorDialog {
            val dialog = ErrorDialog()
            val args = Bundle()
            args.putString("description", description)
            dialog.arguments = args
            return dialog
        }

        fun tag() = this::class.java.simpleName as String
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = AlertDialog.Builder(context).create()
        val binding: DialogErrorBinding =
            DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_error, null, false)
        binding.description.text = this.description
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