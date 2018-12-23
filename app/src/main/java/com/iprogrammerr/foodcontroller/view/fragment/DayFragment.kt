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
import com.iprogrammerr.foodcontroller.databinding.FragmentDayBinding
import com.iprogrammerr.foodcontroller.model.IdTarget
import com.iprogrammerr.foodcontroller.model.day.Day
import com.iprogrammerr.foodcontroller.view.RootView
import com.iprogrammerr.foodcontroller.view.dialog.InformationDialog
import com.iprogrammerr.foodcontroller.view.items.MealsView
import com.iprogrammerr.foodcontroller.viewmodel.DayViewModel

class DayFragment : Fragment(), IdTarget {

    private lateinit var root: RootView
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
        val binding: FragmentDayBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_day, container, false)
        binding.meals.layoutManager = LinearLayoutManager(this.context)
        this.viewModel.day { r ->
            if (r.isSuccess()) {
                drawProgress(r.value())
                binding.meals.adapter = MealsView(r.value().meals(), this)
            } else {
                InformationDialog.new(r.exception()).show(this.childFragmentManager)
            }
        }
        this.root.changeTitle(getString(R.string.day))
        return binding.root
    }

    //TODO draw progress
    private fun drawProgress(day: Day) {

    }

    //TODO, go to meal
    override fun hit(id: Long) {

    }
}