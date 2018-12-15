package com.iprogrammerr.foodcontroller.view.fragment

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.iprogrammerr.foodcontroller.R
import com.iprogrammerr.foodcontroller.databinding.FragmentDayBinding

class DayFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentDayBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_day, container, false)
        return binding.root
    }
}