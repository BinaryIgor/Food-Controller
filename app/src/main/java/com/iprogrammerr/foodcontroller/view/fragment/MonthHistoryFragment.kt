package com.iprogrammerr.foodcontroller.view.fragment

import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.iprogrammerr.foodcontroller.R
import com.iprogrammerr.foodcontroller.databinding.FragmentMonthHistoryBinding
import com.iprogrammerr.foodcontroller.view.RootView

class MonthHistoryFragment : Fragment() {

    private lateinit var root: RootView

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        this.root = context as RootView
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentMonthHistoryBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_month_history, container, false)
        return binding.root
    }
}