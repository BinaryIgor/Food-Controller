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
import com.iprogrammerr.foodcontroller.databinding.FragmentDaysBinding
import com.iprogrammerr.foodcontroller.view.RootView
import com.iprogrammerr.foodcontroller.viewmodel.DaysViewModel
import java.util.*

class DaysFragment : Fragment() {

    private lateinit var root: RootView
    private val viewModel by lazy {
        ViewModelProviders.of(this, DaysViewModel.factory({
            val m = Calendar.getInstance()
            m.timeInMillis = this.arguments!!.getLong("month")
            m
        }())).get(DaysViewModel::class.java)
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
        this.root.changeTitle(getString(R.string.days))
        return binding.root
    }
}