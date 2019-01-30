package com.iprogrammerr.foodcontroller.view.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import com.iprogrammerr.foodcontroller.R
import com.iprogrammerr.foodcontroller.databinding.DialogItemsBinding
import com.iprogrammerr.foodcontroller.model.result.LifecycleCallback
import com.iprogrammerr.foodcontroller.view.items.MealFoodView
import com.iprogrammerr.foodcontroller.viewmodel.MealDetailsViewModel
import java.util.*

class MealDetailsDialog : DialogFragment() {

    private val viewModel by lazy {
        ViewModelProviders.of(
            this, MealDetailsViewModel.factory(this.arguments!!.getLong(ID))
        ).get(MealDetailsViewModel::class.java)
    }

    companion object {

        private const val ID = "ID"

        fun new(id: Long): MealDetailsDialog {
            val dialog = MealDetailsDialog()
            val args = Bundle()
            args.putLong(ID, id)
            dialog.arguments = args
            return dialog
        }

        fun tag() = this::class.java.simpleName as String
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = AlertDialog.Builder(this.context).create()
        val binding: DialogItemsBinding =
            DataBindingUtil.inflate(LayoutInflater.from(this.context), R.layout.dialog_items, null,
                false)
        this.viewModel.meal(LifecycleCallback(this) { r ->
            if (r.isSuccess()) {
                val calendar = Calendar.getInstance()
                calendar.timeInMillis = r.value().time()
                binding.title.text = String.format(
                    getString(R.string.meal_details_template),
                    calendar[Calendar.HOUR_OF_DAY], calendar[Calendar.MINUTE]
                )
                binding.items.layoutManager = LinearLayoutManager(this.context)
                binding.items.adapter = MealFoodView(r.value())
            } else {
                Snackbar.make(binding.root, r.exception(), Snackbar.LENGTH_LONG).show()
            }
        })
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