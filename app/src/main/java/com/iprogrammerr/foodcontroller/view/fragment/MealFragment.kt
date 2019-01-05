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
import com.iprogrammerr.foodcontroller.model.result.LifecycleCallback
import com.iprogrammerr.foodcontroller.model.scalar.HourMinutes
import com.iprogrammerr.foodcontroller.view.RootView
import com.iprogrammerr.foodcontroller.view.dialog.ErrorDialog
import com.iprogrammerr.foodcontroller.view.dialog.TimeDialog
import com.iprogrammerr.foodcontroller.view.dialog.TimeTarget
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
        this.mealId = (this.arguments as Bundle).getLong("mealId", -1L)
    }

    //TODO setup time picker, get data from view model
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.binding = DataBindingUtil.inflate(inflater, R.layout.fragment_meal, container, false)
        this.binding.add.setOnClickListener { this.root.replace(FoodFragment(), true) }
        this.binding.timeValue.text = HourMinutes(System.currentTimeMillis()).value()
        this.binding.timeLayout.setOnClickListener {
            TimeDialog.new(System.currentTimeMillis()).show(this.childFragmentManager)
        }
        this.root.changeTitle(getString(R.string.meal))
        return this.binding.root
    }

    override fun hit(time: Long) {
        this.binding.timeValue.text = HourMinutes(time).value()
        if (this.mealId != -1L) {
            this.viewModel.changeTime(time, LifecycleCallback(this) { r ->
                if (!r.isSuccess()) {
                    ErrorDialog.new(r.exception()).show(this.childFragmentManager)
                }
            })
        }
    }
}