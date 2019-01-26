package com.iprogrammerr.foodcontroller.view.fragment

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.iprogrammerr.foodcontroller.R
import com.iprogrammerr.foodcontroller.databinding.FragmentMealBinding
import com.iprogrammerr.foodcontroller.model.food.Food
import com.iprogrammerr.foodcontroller.model.result.LifecycleCallback
import com.iprogrammerr.foodcontroller.model.scalar.HourMinutes
import com.iprogrammerr.foodcontroller.view.RootView
import com.iprogrammerr.foodcontroller.view.dialog.ErrorDialog
import com.iprogrammerr.foodcontroller.view.dialog.TimeDialog
import com.iprogrammerr.foodcontroller.view.dialog.TimeTarget
import com.iprogrammerr.foodcontroller.view.items.FoodWithActionTarget
import com.iprogrammerr.foodcontroller.view.items.MealFoodView
import com.iprogrammerr.foodcontroller.view.items.WithActionTarget
import com.iprogrammerr.foodcontroller.view.message.Message
import com.iprogrammerr.foodcontroller.view.message.MessageTarget
import com.iprogrammerr.foodcontroller.viewmodel.MealViewModel

class MealFragment : Fragment(), TimeTarget, MessageTarget, FoodWithActionTarget {

    private lateinit var root: RootView
    private lateinit var binding: FragmentMealBinding
    private var mealId = -1L
    private val viewModel by lazy {
        if (this.mealId == -1L) {
            ViewModelProviders.of(this).get(MealViewModel::class.java)
        } else {
            ViewModelProviders.of(this, MealViewModel.factory(this.mealId))
                .get(MealViewModel::class.java)
        }
    }

    companion object {
        fun withMealId(id: Long) = withId(id, "mealId")

        fun withDayId(id: Long) = withId(id, "dayId")

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
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val args = this.arguments as Bundle
        this.mealId = args.getLong("mealId", -1L)
        if (!hasMeal()) {
            args.putLong("time", System.currentTimeMillis())
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        this.binding = DataBindingUtil.inflate(inflater, R.layout.fragment_meal, container, false)
        this.binding.add.setOnClickListener { addFoodOr() }
        setupTimeView()
        this.root.changeTitle(getString(R.string.meal))
        return this.binding.root
    }

    private fun addFoodOr() {
        if (hasMeal()) {
            this.root.replace(FoodFragment.new(this.mealId), true)
        } else {
            val args = this.arguments as Bundle
            this.viewModel.create(args.getLong("time"),
                args.getLong("dayId"),
                LifecycleCallback(this) { r ->
                    if (r.isSuccess()) {
                        args.putLong("mealId", r.value())
                        this.mealId = r.value()
                        this.root.replace(FoodFragment.new(this.mealId), true)
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
                    this.binding.food.layoutManager = LinearLayoutManager(this.context)
                    this.binding.food.adapter = MealFoodView(r.value(), this)
                } else {
                    ErrorDialog.new(r.exception()).show(this.childFragmentManager)
                }
            })
        } else {
            val time = (this.arguments as Bundle).getLong("time")
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
                }
            })
        } else {
            this.arguments?.putLong("time", time)
        }
    }

    override fun hit(message: Message) {
        if (message == Message.PORTIONS_CHANGED) {
            this.viewModel.refresh()
            this.root.propagate(Message.MEALS_CHANGED)
        }
    }

    private fun hasMeal() = this.mealId != -1L

    override fun hit(item: Food, action: WithActionTarget.Action) {
        if (action == WithActionTarget.Action.EDIT) {
            this.root.replace(
                FoodPortionFragment.withWeight(item.definition().id(), this.mealId, item.weight()), true
            )
        } else {
            this.viewModel.removeFood(item.id(), LifecycleCallback(this) { r1 ->
                if (r1.isSuccess()) {
                    this.viewModel.meal(LifecycleCallback(this) { r2 ->
                        if (r2.isSuccess()) {
                            this.binding.food.adapter = MealFoodView(r2.value(), this)
                            this.root.propagate(Message.MEALS_CHANGED)
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