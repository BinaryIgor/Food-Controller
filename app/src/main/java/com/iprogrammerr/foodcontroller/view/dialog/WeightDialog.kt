package com.iprogrammerr.foodcontroller.view.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.view.LayoutInflater
import android.widget.NumberPicker
import com.iprogrammerr.foodcontroller.R
import com.iprogrammerr.foodcontroller.databinding.DialogWeightBinding
import kotlin.math.roundToInt

class WeightDialog : DialogFragment() {

    private lateinit var target: Target

    companion object {

        private const val MIN_WEIGHT = "MIN_WEIGHT"
        private const val MAX_WEIGHT = "MAX_WEIGHT"
        private const val WEIGHT = "WEIGHT"

        fun new(minWeight: Int, maxWeight: Int, weight: Double): WeightDialog {
            val dialog = WeightDialog()
            val args = Bundle()
            args.putInt(MIN_WEIGHT, minWeight)
            args.putInt(MAX_WEIGHT, maxWeight)
            args.putDouble(WEIGHT, weight)
            dialog.arguments = args
            return dialog
        }

        fun new(weight: Double) = new(40, 200, weight)

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
        val binding: DialogWeightBinding = DataBindingUtil.inflate(
            LayoutInflater.from(this.context),
            R.layout.dialog_weight,
            null,
            false
        )
        val args = this.arguments as Bundle
        val minWeight = args.getInt(MIN_WEIGHT)
        val maxWeight = args.getInt(MAX_WEIGHT)
        setupPicker(binding.kg, minWeight, maxWeight)
        setupPicker(binding.g, 0, 9)
        val weight = args.getDouble(WEIGHT)
        val kilograms: Int = weight.toInt()
        val grams: Int = ((weight - kilograms) * 10).roundToInt() % 10
        binding.kg.value = kilograms
        binding.g.value = grams
        binding.cancel.setOnClickListener { dialog.dismiss() }
        binding.ok.setOnClickListener {
            dismiss()
            binding.kg.clearFocus()
            binding.g.clearFocus()
            this.target.hit(binding.kg.value + (binding.g.value / 10.0))
        }
        dialog.setView(binding.root)
        return dialog
    }

    private fun setupPicker(picker: NumberPicker, min: Int, max: Int) {
        picker.wrapSelectorWheel = true
        picker.displayedValues = Array(max - min + 1) { v -> "${min + v}" }
        picker.minValue = min
        picker.maxValue = max
    }

    fun show(manager: FragmentManager) {
        if (manager.findFragmentByTag(tag()) == null) {
            show(manager, tag())
        }
    }

    interface Target {
        fun hit(weight: Double)
    }
}