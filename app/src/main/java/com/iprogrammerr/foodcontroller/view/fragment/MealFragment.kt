package com.iprogrammerr.foodcontroller.view.fragment

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.iprogrammerr.foodcontroller.R
import com.iprogrammerr.foodcontroller.databinding.FragmentMealBinding
import com.iprogrammerr.foodcontroller.model.food.Food
import com.iprogrammerr.foodcontroller.model.result.LifecycleCallback
import com.iprogrammerr.foodcontroller.model.scalar.GridOrLinear
import com.iprogrammerr.foodcontroller.model.scalar.HourMinutes
import com.iprogrammerr.foodcontroller.view.RootView
import com.iprogrammerr.foodcontroller.view.dialog.ErrorDialog
import com.iprogrammerr.foodcontroller.view.dialog.TimeDialog
import com.iprogrammerr.foodcontroller.view.items.FoodWithActionTarget
import com.iprogrammerr.foodcontroller.view.items.MealFoodView
import com.iprogrammerr.foodcontroller.view.items.WithActionTarget
import com.iprogrammerr.foodcontroller.view.message.Message
import com.iprogrammerr.foodcontroller.view.message.MessageTarget
import com.iprogrammerr.foodcontroller.viewmodel.MealViewModel
import java.util.*

class MealFragment : Fragment(), TimeDialog.Target, MessageTarget, FoodWithActionTarget {

    private lateinit var root: RootView
    private lateinit var binding: FragmentMealBinding
    private lateinit var viewModel: MealViewModel

    companion object {

        private const val MEAL_ID = "MEAL_ID"
        private const val DAY_ID = "DAY_ID"
        private const val TIME = "TIME"
        private const val REFRESH = "REFRESH"

        fun withMealId(id: Long) = withId(id, MEAL_ID)

        fun withDayId(id: Long, date: Long): MealFragment {
            val fragment = withId(id, DAY_ID)
            val current = Calendar.getInstance()
            val meal = Calendar.getInstance()
            meal.timeInMillis = date
            meal[Calendar.HOUR_OF_DAY] = current[Calendar.HOUR_OF_DAY]
            meal[Calendar.MINUTE] = current[Calendar.MINUTE]
            meal[Calendar.SECOND] = current[Calendar.SECOND]
            fragment.arguments!!.putLong(TIME, meal.timeInMillis)
            return fragment
        }

        private fun withId(id: Long, key: String): MealFragment {
            val fragment = MealFragment()
            val args = Bundle()
            args.putLong(key, id)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        this.root = context as RootView
        val mealId = this.arguments!!.getLong(MEAL_ID, -1)
        this.viewModel = if (mealId < 0) {
            ViewModelProviders.of(this).get(MealViewModel::class.java)
        } else {
            ViewModelProviders.of(this, MealViewModel.factory(mealId))
                .get(MealViewModel::class.java)
        }
        if (this.arguments!!.getBoolean(REFRESH, false)) {
            this.viewModel.refresh()
            this.arguments!!.putBoolean(REFRESH, false)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        this.binding = DataBindingUtil.inflate(inflater, R.layout.fragment_meal, container, false)
        setupTimeView()
        this.binding.add.setOnClickListener { addFood() }
        this.binding.last.setOnClickListener { addMeal() }
        this.binding.food.layoutManager = GridOrLinear(requireContext(), this.binding.food).value()
        this.root.changeTitle(getString(R.string.meal))
        return this.binding.root
    }

    private fun addFood() {
        val args = this.arguments as Bundle
        if (hasMeal()) {
            this.root.replace(FoodFragment.new(args.getLong(MEAL_ID)), true)
        } else {
            this.viewModel.create(args.getLong(TIME), args.getLong(DAY_ID),
                LifecycleCallback(this) { r ->
                    if (r.isSuccess()) {
                        args.putLong(MEAL_ID, r.value())
                        this.root.replace(FoodFragment.new(args.getLong(MEAL_ID)), true)
                    } else {
                        ErrorDialog.new(r.exception()).show(this.childFragmentManager)
                    }
                })
        }
    }

    private fun addMeal() {
        val args = this.arguments as Bundle
        if (hasMeal()) {
            this.root.replace(LastMealsFragment.new(args.getLong(MEAL_ID)), true)
        } else {
            this.viewModel.create(args.getLong(TIME), args.getLong(DAY_ID),
                LifecycleCallback(this) { r ->
                    if (r.isSuccess()) {
                        args.putLong(MEAL_ID, r.value())
                        this.root.replace(LastMealsFragment.new(args.getLong(MEAL_ID)), true)
                    } else {
                        ErrorDialog.new(r.exception()).show(this.childFragmentManager)
                    }
                })
        }
    }

    private fun setupTimeView() {
        if (hasMeal()) {
            this.viewModel.meal(LifecycleCallback(this) { r ->
                if (r.isSuccess()) {
                    this.binding.timeValue.text = HourMinutes(r.value().time()).value()
                    this.binding.timeLayout.setOnClickListener {
                        TimeDialog.new(r.value().time()).show(this.childFragmentManager)
                    }
                    this.binding.food.adapter = MealFoodView(r.value(), this)
                } else {
                    ErrorDialog.new(r.exception()).show(this.childFragmentManager)
                }
            })
        } else {
            val time = (this.arguments as Bundle).getLong(TIME)
            this.binding.timeValue.text = HourMinutes(time).value()
            this.binding.timeLayout.setOnClickListener {
                TimeDialog.new(time).show(this.childFragmentManager)
            }
        }
    }

    override fun hit(time: Long) {
        this.binding.timeValue.text = HourMinutes(time).value()
        if (hasMeal()) {
            this.viewModel.changeTime(time, LifecycleCallback(this) { r ->
                if (!r.isSuccess()) {
                    ErrorDialog.new(r.exception())
                        .show(this.childFragmentManager)
                } else {
                    this.root.propagate(Message.MEAL_CHANGED)
                }
            })
        } else {
            this.arguments?.putLong(TIME, time)
        }
    }

    override fun hit(message: Message) {
        if (message == Message.PORTIONS_CHANGED || message == Message.MEAL_CHANGED) {
            if (this::viewModel.isInitialized) {
                this.viewModel.refresh()
            } else {
                this.arguments!!.putBoolean(REFRESH, true)
            }
        }
    }

    private fun hasMeal() = this.arguments!!.getLong(MEAL_ID, -1) > -1

    override fun hit(item: Food, action: WithActionTarget.Action) {
        if (action == WithActionTarget.Action.EDIT) {
            this.root.replace(
                FoodPortionFragment.withWeight(item.definitionId(),
                    this.arguments!!.getLong(MEAL_ID), item.weight()),
                true
            )
        } else {
            this.viewModel.removeFood(item.id(), LifecycleCallback(this) { r1 ->
                if (r1.isSuccess()) {
                    this.viewModel.meal(LifecycleCallback(this) { r2 ->
                        if (r2.isSuccess()) {
                            this.binding.food.adapter = MealFoodView(r2.value(), this)
                            this.root.propagate(Message.MEAL_CHANGED)
                        } else {
                            ErrorDialog.new(r1.exception()).show(this.childFragmentManager)
                        }
                    })
                } else {
                    ErrorDialog.new(r1.exception()).show(this.childFragmentManager)
                }
            })
        }
    }
}