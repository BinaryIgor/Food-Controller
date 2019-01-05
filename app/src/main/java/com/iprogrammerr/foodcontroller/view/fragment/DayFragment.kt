package com.iprogrammerr.foodcontroller.view.fragment

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.iprogrammerr.foodcontroller.R
import com.iprogrammerr.foodcontroller.databinding.FragmentDayBinding
import com.iprogrammerr.foodcontroller.model.IdTarget
import com.iprogrammerr.foodcontroller.model.NutritionalValues
import com.iprogrammerr.foodcontroller.model.day.Day
import com.iprogrammerr.foodcontroller.model.result.LifecycleCallback
import com.iprogrammerr.foodcontroller.model.result.Result
import com.iprogrammerr.foodcontroller.view.RootView
import com.iprogrammerr.foodcontroller.view.dialog.ErrorDialog
import com.iprogrammerr.foodcontroller.view.dialog.WeightDialog
import com.iprogrammerr.foodcontroller.view.dialog.WeightTarget
import com.iprogrammerr.foodcontroller.view.items.MealsView
import com.iprogrammerr.foodcontroller.viewmodel.DayViewModel

class DayFragment : Fragment(), IdTarget, WeightTarget {

    private lateinit var root: RootView
    private lateinit var binding: FragmentDayBinding
    private val viewModel: DayViewModel by lazy {
        ViewModelProviders.of(this).get(DayViewModel::class.java)
    }

    companion object {
        fun new(date: Long): DayFragment {
            val fragment = DayFragment()
            val args = Bundle()
            args.putLong("date", date)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        this.root = context as RootView
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.binding = DataBindingUtil.inflate(inflater, R.layout.fragment_day, container, false)
        this.binding.meals.layoutManager = LinearLayoutManager(this.context)
        this.viewModel.day(LifecycleCallback(this) { r -> onDayResult(r) })
        this.root.changeTitle(getString(R.string.day))
        return this.binding.root
    }

    private fun onDayResult(result: Result<Day>) {
        if (result.isSuccess()) {
            drawProgress(result.value())
            this.binding.meals.adapter = MealsView(result.value().meals(), this)
            this.binding.weight.setOnClickListener {
                WeightDialog.new(result.value().weight()).show(this.childFragmentManager)
            }
            this.binding.add.setOnClickListener { this.root.replace(MealFragment.withDayId(result.value().id()), true) }
        } else {
            ErrorDialog.new(result.exception()).show(this.childFragmentManager)
        }
    }

    //TODO update goals?
    private fun drawProgress(day: Day) {
        val values = eatenValues(day)
        val goals = day.goals()
        this.binding.caloriesProgress.max = goals.calories()
        this.binding.caloriesProgress.progress = values.calories()
        if (values.calories() > goals.calories()) {
            this.binding.caloriesValue.setTextColor(ContextCompat.getColor(this.context as Context, R.color.invalid))
        }
        this.binding.caloriesValue.text = "${values.calories()}/${goals.calories()}"
        this.binding.proteinProgress.max = goals.protein()
        this.binding.proteinProgress.progress = values.protein()
        this.binding.proteinValue.text = "${values.protein()}/${goals.protein()}"

    }

    private fun eatenValues(day: Day): NutritionalValues {
        var calories = 0
        var protein = 0
        for (m in day.meals()) {
            val values = m.nutritionalValues()
            calories += values.calories()
            protein += values.protein()
        }
        return object : NutritionalValues {
            override fun calories() = calories

            override fun protein() = protein
        }
    }

    override fun hit(id: Long) {
        this.root.replace(MealFragment.withMealId(id), true)
    }

    override fun hit(weight: Double) {
        this.viewModel.changeWeight(weight, LifecycleCallback(this) { r ->
            if (!r.isSuccess()) {
                ErrorDialog.new(r.exception()).show(this.childFragmentManager)
            }
        })
    }
}