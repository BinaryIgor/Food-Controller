package com.iprogrammerr.foodcontroller.view.fragment

import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.iprogrammerr.foodcontroller.R
import com.iprogrammerr.foodcontroller.databinding.FragmentFoodPortionBinding
import com.iprogrammerr.foodcontroller.view.RootView

class FoodPortionFragment : Fragment() {

    private lateinit var root: RootView

    companion object {
        fun new(id: Long): FoodPortionFragment {
            val fragment = FoodPortionFragment()
            val args = Bundle()
            args.putLong("id", id)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        this.root = context as RootView
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentFoodPortionBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_food_portion, container, false)
        return binding.root
    }
}