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
import com.iprogrammerr.foodcontroller.databinding.FragmentCategoriesBinding
import com.iprogrammerr.foodcontroller.model.IdTarget
import com.iprogrammerr.foodcontroller.model.result.LifecycleCallback
import com.iprogrammerr.foodcontroller.view.RootView
import com.iprogrammerr.foodcontroller.view.dialog.ErrorDialog
import com.iprogrammerr.foodcontroller.view.items.CategoriesView
import com.iprogrammerr.foodcontroller.viewmodel.CategoriesViewModel

class CategoriesFragment : Fragment(), IdTarget {

    private lateinit var root: RootView
    private val viewModel by lazy {
        ViewModelProviders.of(this).get(CategoriesViewModel::class.java)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        this.root = context as RootView
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentCategoriesBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_categories, container, false)
        binding.categories.layoutManager = LinearLayoutManager(this.context)
        this.viewModel.categories(LifecycleCallback(this) { r ->
            if (r.isSuccess()) {
                binding.categories.adapter = CategoriesView(r.value(), this)
            } else {
                ErrorDialog.new(r.exception()).show(this.childFragmentManager)
            }
        })
        this.root.changeTitle(getString(R.string.categories))
        return binding.root
    }

    override fun hit(id: Long) {
        this.root.replace(CategoryFoodDefinitionsFragment.new(id), true)
    }
}