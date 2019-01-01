package com.iprogrammerr.foodcontroller.view.fragment

import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.iprogrammerr.foodcontroller.R
import com.iprogrammerr.foodcontroller.databinding.FragmentGoalsBinding
import com.iprogrammerr.foodcontroller.model.DoubleFromView
import com.iprogrammerr.foodcontroller.model.IntFromView
import com.iprogrammerr.foodcontroller.view.RootView
import com.iprogrammerr.foodcontroller.view.dialog.InformationDialog

//TODO with viewModel communication
class GoalsFragment : Fragment() {

    private lateinit var root: RootView
    private lateinit var binding: FragmentGoalsBinding

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        this.root = context as RootView
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.binding = DataBindingUtil.inflate(inflater, R.layout.fragment_goals, container, false)
        this.binding.save.setOnClickListener { save() }
        this.root.changeTitle(getString(R.string.goals))
        return this.binding.root
    }

    private fun save() {
        val weight = DoubleFromView(this.binding.weightInput).value()
        val calories = IntFromView(this.binding.caloriesInput).value()
        val protein = IntFromView(this.binding.proteinInput).value()
        when {
            weight < 1 -> InformationDialog.new(getString(R.string.weight_invalid)).show(this.childFragmentManager)
            calories < 1 -> InformationDialog.new(getString(R.string.calories_invalid)).show(this.childFragmentManager)
            protein < 1 -> InformationDialog.new(getString(R.string.protein_invalid)).show(this.childFragmentManager)
        }
    }
}