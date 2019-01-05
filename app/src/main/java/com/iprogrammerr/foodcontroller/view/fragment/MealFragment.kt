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
import com.iprogrammerr.foodcontroller.view.RootView
import com.iprogrammerr.foodcontroller.viewmodel.MealViewModel

class MealFragment : Fragment() {

    private lateinit var root: RootView
    private val viewModel by lazy {
        val args = this.arguments as Bundle
        if (args.getLong("mealId", 0L) == 0L) {
            ViewModelProviders.of(this).get(MealViewModel::class.java)
        } else {
            ViewModelProviders.of(this, MealViewModel.factory(args.getLong("mealId")))
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

    //TODO setup time picker, get data from view model
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentMealBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_meal, container, false)
        this.root.changeTitle(getString(R.string.meal))
        return binding.root
    }
}