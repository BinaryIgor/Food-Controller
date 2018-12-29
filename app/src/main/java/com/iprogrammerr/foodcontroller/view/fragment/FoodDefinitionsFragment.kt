package com.iprogrammerr.foodcontroller.view.fragment

import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.iprogrammerr.foodcontroller.R
import com.iprogrammerr.foodcontroller.databinding.FragmentFoodDefinitionsBinding
import com.iprogrammerr.foodcontroller.view.RootView

class FoodDefinitionsFragment : Fragment() {

    private lateinit var root: RootView

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        this.root = context as RootView
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentFoodDefinitionsBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_food_definitions, container, false)
        this.root.changeTitle(getString(R.string.products))
        return binding.root
    }
}