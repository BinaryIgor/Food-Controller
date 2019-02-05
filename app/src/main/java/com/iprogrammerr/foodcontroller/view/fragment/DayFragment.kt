package com.iprogrammerr.foodcontroller.view.fragment

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.iprogrammerr.foodcontroller.R
import com.iprogrammerr.foodcontroller.databinding.FragmentDayBinding
import com.iprogrammerr.foodcontroller.model.NutritionalValues
import com.iprogrammerr.foodcontroller.model.day.Day
import com.iprogrammerr.foodcontroller.model.result.LifecycleCallback
import com.iprogrammerr.foodcontroller.model.result.Result
import com.iprogrammerr.foodcontroller.model.scalar.GridOrLinear
import com.iprogrammerr.foodcontroller.view.RootView
import com.iprogrammerr.foodcontroller.view.dialog.*
import com.iprogrammerr.foodcontroller.view.items.IdWithActionTarget
import com.iprogrammerr.foodcontroller.view.items.MealsView
import com.iprogrammerr.foodcontroller.view.items.WithActionTarget
import com.iprogrammerr.foodcontroller.view.message.Message
import com.iprogrammerr.foodcontroller.view.message.MessageTarget
import com.iprogrammerr.foodcontroller.viewmodel.DayViewModel
import kotlin.math.roundToInt

class DayFragment : Fragment(), IdWithActionTarget, WeightDialog.Target, TwoOptionsDialog.Target, MessageTarget {

    private lateinit var root: RootView
    private lateinit var binding: FragmentDayBinding
    private lateinit var viewModel: DayViewModel

    companion object {

        private const val DELETE_DAY = "DELETE_DAY"
        private const val DATE = "DATE"
        private const val MEAL_ID = "MEAL_ID"

        fun new(date: Long): DayFragment {
            val fragment = DayFragment()
            val args = Bundle()
            args.putLong(DATE, date)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        this.root = context as RootView
        this.arguments = this.arguments?.let { it } ?: Bundle()
        this.viewModel = ViewModelProviders.of(
            this,
            DayViewModel.factory(
                this.arguments!!.getLong(DATE, System.currentTimeMillis())
            )
        ).get(DayViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        this.binding = DataBindingUtil.inflate(inflater, R.layout.fragment_day, container, false)
        this.binding.meals.layoutManager = GridOrLinear(
            requireContext(),
            this.binding.meals
        ).value()
        this.viewModel.day(LifecycleCallback(this) { r -> onDayResult(r) })
        this.root.changeTitle(this.viewModel.formats.date().format(this.arguments!!.getLong(DATE)))
        return this.binding.root
    }

    private fun onDayResult(result: Result<Day>) {
        if (result.isSuccess()) {
            drawProgress(result.value())
            this.binding.meals.adapter = MealsView(result.value().meals(), this)
            this.binding.weight.setOnClickListener {
                WeightDialog.new(result.value().weight()).show(this.childFragmentManager)
            }
            this.binding.add.setOnClickListener {
                this.root.replace(MealFragment.withDayId(result.value().id()), true)
            }
            this.binding.goals.setOnClickListener {
                DayGoalsDialog.new(this.arguments!!.getLong(DATE)).show(requireFragmentManager())
            }
        } else {
            ErrorDialog.new(result.exception()).show(this.childFragmentManager)
        }
    }

    private fun drawProgress(day: Day) {
        val values = eatenValues(day)
        val goals = day.goals()
        this.binding.caloriesProgress.max = goals.calories()
        this.binding.caloriesProgress.progress = values.calories()
        if (values.calories() > goals.calories()) {
            this.binding.caloriesValue.setTextColor(
                ContextCompat.getColor(this.context as Context, R.color.invalid)
            )
        }
        this.binding.caloriesValue.text = "${values.calories()}/${goals.calories()}"
        this.binding.proteinProgress.max = goals.protein().roundToInt()
        this.binding.proteinProgress.progress = values.protein().roundToInt()
        this.binding.proteinValue.text = "${values.protein().roundToInt()}/${goals.protein().roundToInt()}"
    }

    private fun eatenValues(day: Day): NutritionalValues {
        var calories = 0
        var protein = 0.0
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

    override fun hit(item: Long, action: WithActionTarget.Action) {
        when (action) {
            WithActionTarget.Action.DETAILS -> {
                MealDetailsDialog.new(item).show(this.childFragmentManager)
            }
            WithActionTarget.Action.EDIT -> {
                this.root.replace(MealFragment.withMealId(item), true)
            }
            else -> {
                this.arguments?.putLong(MEAL_ID, item)
                this.arguments?.putBoolean(DELETE_DAY, false)
                TwoOptionsDialog.new(
                    getString(R.string.delete_meal_confirmation), getString(R.string.cancel),
                    getString(R.string.ok)
                ).show(this.childFragmentManager)
            }
        }
    }

    override fun hit(weight: Double) {
        this.viewModel.changeWeight(weight, LifecycleCallback(this) { r ->
            if (!r.isSuccess()) {
                ErrorDialog.new(r.exception()).show(this.childFragmentManager)
            }
        })
    }

    override fun hitLeft() {

    }

    override fun hitRight() {
        val args = this.arguments as Bundle
        if (args.getBoolean(DELETE_DAY)) {
            this.viewModel.deleteDay(LifecycleCallback(this) { r ->
                if (r.isSuccess()) {
                    this.root.propagate(Message.DAYS_CHANGED)
                    requireFragmentManager().popBackStack()
                } else {
                    ErrorDialog.new(r.exception()).show(this.childFragmentManager)
                }
            })
        } else {
            this.viewModel.deleteMeal((this.arguments as Bundle).getLong(MEAL_ID),
                LifecycleCallback(this) { r1 ->
                    if (r1.isSuccess()) {
                        this.root.propagate(Message.MEALS_CHANGED)
                        this.viewModel.day(LifecycleCallback(this) { r2 -> onDayResult(r2) })
                    } else {
                        ErrorDialog.new(r1.exception()).show(this.childFragmentManager)
                    }
                })
        }
    }

    override fun hit(message: Message) {
        if (this::viewModel.isInitialized && (message == Message.MEALS_CHANGED || message == Message.GOALS_CHANGED)) {
            this.viewModel.refresh()
            this.root.propagate(Message.DAYS_CHANGED)
            if (this.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                this.viewModel.day(LifecycleCallback(this) { r ->
                    onDayResult(r)
                })
            }
        } else if (message == Message.DELETE_DAY_CLICKED) {
            this.arguments?.putBoolean(DELETE_DAY, true)
            TwoOptionsDialog.new(
                getString(R.string.delete_day_confirmation),
                getString(R.string.cancel),
                getString(R.string.ok)
            ).show(this.childFragmentManager)
        }
    }
}