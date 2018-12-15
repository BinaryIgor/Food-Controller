package com.iprogrammerr.foodcontroller.fragment

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.iprogrammerr.foodcontroller.R
import com.iprogrammerr.foodcontroller.databinding.FragmentCategoriesBinding

class CategoryProductsFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentCategoriesBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_category_products, container, false)
        return binding.root
    }
}