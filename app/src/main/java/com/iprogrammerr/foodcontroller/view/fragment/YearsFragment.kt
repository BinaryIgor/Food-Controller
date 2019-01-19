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
import com.iprogrammerr.foodcontroller.databinding.FragmentYearsBinding
import com.iprogrammerr.foodcontroller.model.result.LifecycleCallback
import com.iprogrammerr.foodcontroller.view.RootView
import com.iprogrammerr.foodcontroller.view.dialog.ErrorDialog
import com.iprogrammerr.foodcontroller.view.items.AdapterTarget
import com.iprogrammerr.foodcontroller.view.items.YearsView
import com.iprogrammerr.foodcontroller.viewmodel.YearsViewModel

class YearsFragment : Fragment(), AdapterTarget<Int> {

    private lateinit var root: RootView
    private val viewModel by lazy {
        ViewModelProviders.of(this).get(YearsViewModel::class.java)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        this.root = context as RootView
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        val binding: FragmentYearsBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_years, container, false
        )
        this.viewModel.years(LifecycleCallback(this) { r ->
            if (r.isSuccess()) {
                binding.years.layoutManager = LinearLayoutManager(this.context)
                binding.years.adapter = YearsView(r.value(), this)
            } else {
                ErrorDialog.new(r.exception()).show(this.childFragmentManager)
            }
        })
        this.root.changeTitle(getString(R.string.years))
        return binding.root
    }

    override fun hit(item: Int) {
        this.root.replace(MonthsFragment.new(item), true)
    }
}