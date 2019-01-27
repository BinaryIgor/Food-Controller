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
import com.iprogrammerr.foodcontroller.ObjectsPool
import com.iprogrammerr.foodcontroller.R
import com.iprogrammerr.foodcontroller.databinding.FragmentDaysBinding
import com.iprogrammerr.foodcontroller.model.IdTarget
import com.iprogrammerr.foodcontroller.model.format.Formats
import com.iprogrammerr.foodcontroller.model.result.LifecycleCallback
import com.iprogrammerr.foodcontroller.view.RootView
import com.iprogrammerr.foodcontroller.view.dialog.ErrorDialog
import com.iprogrammerr.foodcontroller.view.items.DaysView
import com.iprogrammerr.foodcontroller.viewmodel.DaysViewModel
import java.util.*
import kotlin.math.roundToInt

class DaysFragment : Fragment(), IdTarget {

    private lateinit var root: RootView
    private val format = ObjectsPool.single(Formats::class.java).date()
    private val viewModel by lazy {
        val month = Calendar.getInstance()
        month.timeInMillis = this.arguments!!.getLong("month")
        ViewModelProviders.of(this, DaysViewModel.factory(month)).get(DaysViewModel::class.java)
    }

    companion object {
        fun new(month: Calendar): DaysFragment {
            val fragment = DaysFragment()
            val args = Bundle()
            args.putLong("month", month.timeInMillis)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        this.root = context as RootView
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        val binding: FragmentDaysBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_days, container, false
        )
        this.viewModel.averageConsumption(LifecycleCallback(this) { r ->
            if (r.isSuccess()) {
                binding.caloriesValue.text = r.value().calories().toString()
                binding.proteinValue.text = r.value().protein().roundToInt().toString()
            } else {
                ErrorDialog.new(r.exception()).show(this.childFragmentManager)
            }
        })
        this.viewModel.days(LifecycleCallback(this) { r ->
            if (r.isSuccess()) {
                binding.days.layoutManager = LinearLayoutManager(this.context)
                binding.days.adapter = DaysView(this.format, r.value(), this)
            } else {
                ErrorDialog.new(r.exception()).show(this.childFragmentManager)
            }
        })
        this.root.changeTitle(getString(R.string.days))
        return binding.root
    }

    override fun hit(id: Long) {
        println("Day $id")
    }
}