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
import com.iprogrammerr.foodcontroller.databinding.FragmentCategoriesBinding
import com.iprogrammerr.foodcontroller.model.category.Categories
import com.iprogrammerr.foodcontroller.pool.ObjectsPool
import com.iprogrammerr.foodcontroller.view.RootView
import com.iprogrammerr.foodcontroller.view.dialog.InformationDialog
import com.iprogrammerr.foodcontroller.viewmodel.CategoriesViewModel
import com.iprogrammerr.foodcontroller.viewmodel.factory.CategoriesViewModelFactory
import java.util.concurrent.Executor

class CategoriesFragment : Fragment() {

    private lateinit var root: RootView
    private val viewModel by lazy {
        ViewModelProviders.of(
            this,
            CategoriesViewModelFactory(
                ObjectsPool.single(Executor::class.java),
                ObjectsPool.single(Categories::class.java)
            )
        ).get(CategoriesViewModel::class.java)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        this.root = context as RootView
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentCategoriesBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_categories, container, false)
        this.viewModel.categories { r ->
            if (r.isSuccess()) {
                ///TODO draw adapter
            } else {
                InformationDialog.new(r.exception()).show(this.childFragmentManager)
            }
        }
        this.root.changeTitle(getString(R.string.categories))
        return binding.root
    }
}