package com.iprogrammerr.foodcontroller.view.fragment

import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.iprogrammerr.foodcontroller.R
import com.iprogrammerr.foodcontroller.databinding.FragmentMenuBinding
import com.iprogrammerr.foodcontroller.view.RootView

class MenuFragment : Fragment() {

    private lateinit var root: RootView

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        this.root = context as RootView
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentMenuBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_menu, container, false)
        this.root.changeTitle(getString(R.string.menu))
        binding.day.setOnClickListener { _ -> this.root.replace(DayFragment(), true) }
        binding.history.setOnClickListener { _ -> this.root.replace(HistoryFragment(), true) }
        binding.base.setOnClickListener { _ -> this.root.replace(CategoriesFragment(), true) }
        binding.goals.setOnClickListener { _ -> this.root.replace(GoalsFragment(), true) }
        return binding.root
    }
}
