package com.iprogrammerr.foodcontroller.view.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.view.LayoutInflater
import com.iprogrammerr.foodcontroller.R
import com.iprogrammerr.foodcontroller.databinding.DialogDayGoalsBinding
import com.iprogrammerr.foodcontroller.model.NutritionalValues
import com.iprogrammerr.foodcontroller.model.result.LifecycleCallback
import com.iprogrammerr.foodcontroller.model.scalar.IntFromView
import com.iprogrammerr.foodcontroller.view.RootView
import com.iprogrammerr.foodcontroller.view.message.Message
import com.iprogrammerr.foodcontroller.viewmodel.DayGoalsViewModel
import kotlin.math.roundToInt

class DayGoalsDialog : DialogFragment() {

    private lateinit var root: RootView
    private lateinit var binding: DialogDayGoalsBinding
    private lateinit var goals: NutritionalValues
    private val viewModel by lazy {
        ViewModelProviders.of(
            this,
            DayGoalsViewModel.factory(this.arguments!!.getLong(DATE))
        ).get(DayGoalsViewModel::class.java)
    }

    companion object {

        private const val DATE = "DATE"

        fun new(date: Long): DayGoalsDialog {
            val dialog = DayGoalsDialog()
            val args = Bundle()
            args.putLong(DATE, date)
            dialog.arguments = args
            return dialog
        }

        fun tag() = this::class.java.simpleName as String
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        this.root = context as RootView
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = AlertDialog.Builder(this.context).create()
        this.binding = DataBindingUtil.inflate(
            LayoutInflater.from(this.context), R.layout.dialog_day_goals, null, false
        )
        this.viewModel.goals(LifecycleCallback(this) { r ->
            if (r.isSuccess()) {
                this.binding.caloriesInput.setText(r.value().calories().toString())
                this.binding.proteinInput.setText(r.value().protein().roundToInt().toString())
                this.goals = r.value()
            } else {
                Snackbar.make(this.binding.root, r.exception(), Snackbar.LENGTH_LONG).show()
            }
        })
        this.binding.left.setOnClickListener { dialog.dismiss() }
        this.binding.right.setOnClickListener {
            if (this::goals.isInitialized) {
                save()
            }
        }
        dialog.setView(this.binding.root)
        return dialog
    }

    private fun save() {
        val calories = IntFromView(this.binding.caloriesInput).value()
        val protein = IntFromView(this.binding.proteinInput).value()
        when {
            calories < 1 -> Snackbar.make(this.binding.root, getString(R.string.calories_invalid),
                Snackbar.LENGTH_LONG).show()
            protein < 1 -> Snackbar.make(this.binding.root, getString(R.string.protein_invalid),
                Snackbar.LENGTH_LONG).show()
            else -> saveIf(calories, protein)
        }
    }

    private fun saveIf(calories: Int, protein: Int) {
        val changed = calories != this.goals.calories()
            || protein != this.goals.protein().roundToInt()
        if (changed) {
            this.viewModel.save(calories, protein, LifecycleCallback(this) { r ->
                if (r.isSuccess()) {
                    this.root.propagate(Message.GOALS_CHANGED)
                    dismiss()
                } else {
                    Snackbar.make(this.binding.root, r.exception(), Snackbar.LENGTH_LONG).show()
                }
            })
        } else {
            Snackbar.make(this.binding.root, R.string.no_change, Snackbar.LENGTH_LONG).show()
        }
    }

    fun show(manager: FragmentManager) {
        if (manager.findFragmentByTag(tag()) == null) {
            show(manager, tag())
        }
    }
}