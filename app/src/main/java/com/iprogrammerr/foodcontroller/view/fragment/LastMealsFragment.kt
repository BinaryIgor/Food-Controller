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
import com.iprogrammerr.foodcontroller.databinding.FragmentLastMealsBinding
import com.iprogrammerr.foodcontroller.model.meal.Meal
import com.iprogrammerr.foodcontroller.model.result.LifecycleCallback
import com.iprogrammerr.foodcontroller.view.RootView
import com.iprogrammerr.foodcontroller.view.dialog.ErrorDialog
import com.iprogrammerr.foodcontroller.view.items.AdapterTarget
import com.iprogrammerr.foodcontroller.view.items.DetailedMealsView
import com.iprogrammerr.foodcontroller.view.message.Message
import com.iprogrammerr.foodcontroller.viewmodel.LastMealsViewModel

class LastMealsFragment : Fragment(), AdapterTarget<Meal> {

    private lateinit var root: RootView
    private lateinit var binding: FragmentLastMealsBinding
    private val viewModel by lazy {
        ViewModelProviders.of(
            this,
            LastMealsViewModel.factory(this.arguments!!.getLong(MEAL_ID), 15)
        ).get(LastMealsViewModel::class.java)
    }

    companion object {

        private const val MEAL_ID = "MEAL_ID"

        fun new(mealId: Long): LastMealsFragment {
            val fragment = LastMealsFragment()
            val args = Bundle()
            args.putLong(MEAL_ID, mealId)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        this.root = context as RootView
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        this.binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_last_meals, container, false
        )
        this.viewModel.last(LifecycleCallback(this) { r ->
            if (r.isSuccess()) {
                draw(r.value())
            } else {
                ErrorDialog.new(r.exception()).show(this.childFragmentManager)
            }
        })
        this.root.changeTitle(getString(R.string.last_meals))
        return this.binding.root
    }

    private fun draw(meals: List<Meal>) {
        if (meals.isEmpty()) {
            this.binding.noMeals.visibility = View.VISIBLE
            this.binding.ok.visibility = View.VISIBLE
            this.binding.ok.setOnClickListener { requireFragmentManager().popBackStack() }
        } else {
            this.binding.meals.layoutManager = LinearLayoutManager(this.context)
            this.binding.meals.adapter = DetailedMealsView(meals, this)
        }
    }

    override fun hit(item: Meal) {
        this.viewModel.clone(item, LifecycleCallback(this) { r ->
            if (r.isSuccess()) {
                this.root.propagate(Message.MEAL_CHANGED)
                requireFragmentManager().popBackStack()
            } else {
                ErrorDialog.new(r.exception()).show(this.childFragmentManager)
            }
        })
    }
}