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
import com.iprogrammerr.foodcontroller.model.IdTarget
import com.iprogrammerr.foodcontroller.model.meal.Meal
import com.iprogrammerr.foodcontroller.model.result.LifecycleCallback
import com.iprogrammerr.foodcontroller.model.scalar.HourMinutes
import com.iprogrammerr.foodcontroller.view.RootView
import com.iprogrammerr.foodcontroller.view.dialog.ErrorDialog
import com.iprogrammerr.foodcontroller.view.dialog.TimeDialog
import com.iprogrammerr.foodcontroller.view.dialog.TimeTarget
import com.iprogrammerr.foodcontroller.view.items.MealFoodView
import com.iprogrammerr.foodcontroller.viewmodel.MealViewModel

class MealFragment : Fragment(), TimeTarget {

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

    //TODO setup time picker, get data from view model
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
                    setupFoodView(r.value())
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

    private fun setupFoodView(meal: Meal) {
        this.binding.food.layoutManager = LinearLayoutManager(this.context)
        this.binding.food.adapter = MealFoodView(meal, object : IdTarget {

            override fun hit(id: Long) {
                //TODO reimplement this interface to allow popup menu with edit/delete
            }
        })
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
            this.arguments!!.putLong("time", time)
        }
    }

    private fun hasMeal() = this.mealId != -1L
}