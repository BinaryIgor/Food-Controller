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
import com.iprogrammerr.foodcontroller.databinding.FragmentMonthsBinding
import com.iprogrammerr.foodcontroller.model.result.LifecycleCallback
import com.iprogrammerr.foodcontroller.view.RootView
import com.iprogrammerr.foodcontroller.view.dialog.ErrorDialog
import com.iprogrammerr.foodcontroller.view.items.AdapterTarget
import com.iprogrammerr.foodcontroller.view.items.MonthsView
import com.iprogrammerr.foodcontroller.view.message.Message
import com.iprogrammerr.foodcontroller.view.message.MessageTarget
import com.iprogrammerr.foodcontroller.viewmodel.MonthsViewModel
import java.util.*

class MonthsFragment : Fragment(), AdapterTarget<Calendar>, MessageTarget {

    private lateinit var root: RootView
    private val viewModel by lazy {
        ViewModelProviders.of(
            this, MonthsViewModel.factory(this.arguments!!.getInt(YEAR))
        ).get(MonthsViewModel::class.java)
    }

    companion object {

        private const val YEAR = "YEAR"

        fun new(year: Int): MonthsFragment {
            val fragment = MonthsFragment()
            val args = Bundle()
            args.putInt(YEAR, year)
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
        val binding: FragmentMonthsBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_months, container, false
        )
        this.viewModel.months(LifecycleCallback(this) { r ->
            if (r.isSuccess()) {
                if (r.value().isEmpty()) {
                    requireFragmentManager().popBackStack()
                } else {
                    binding.months.layoutManager = LinearLayoutManager(this.context)
                    binding.months.adapter = MonthsView(r.value(), this)
                }
            } else {
                ErrorDialog.new(r.exception()).show(this.childFragmentManager)
            }
        })
        this.root.changeTitle(getString(R.string.months))
        return binding.root
    }

    override fun hit(item: Calendar) {
        this.root.replace(DaysFragment.new(item), true)
    }

    override fun hit(message: Message) {
        if (message == Message.DAYS_CHANGED) {
            this.viewModel.refresh()
        }
    }
}