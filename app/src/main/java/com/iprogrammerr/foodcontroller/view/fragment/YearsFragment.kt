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
import com.iprogrammerr.foodcontroller.databinding.FragmentYearsBinding
import com.iprogrammerr.foodcontroller.model.result.LifecycleCallback
import com.iprogrammerr.foodcontroller.model.scalar.GridOrLinear
import com.iprogrammerr.foodcontroller.view.RootView
import com.iprogrammerr.foodcontroller.view.dialog.ErrorDialog
import com.iprogrammerr.foodcontroller.view.items.AdapterTarget
import com.iprogrammerr.foodcontroller.view.items.YearsView
import com.iprogrammerr.foodcontroller.view.message.Message
import com.iprogrammerr.foodcontroller.view.message.MessageTarget
import com.iprogrammerr.foodcontroller.viewmodel.YearsViewModel

private const val REFRESH = "REFRESH"

class YearsFragment : Fragment(), AdapterTarget<Int>, MessageTarget {

    private lateinit var root: RootView
    private lateinit var binding: FragmentYearsBinding
    private lateinit var viewModel: YearsViewModel

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        this.root = context as RootView
        this.viewModel = ViewModelProviders.of(this).get(YearsViewModel::class.java)
        this.arguments = this.arguments?.let { it } ?: Bundle()
        if (this.arguments!!.getBoolean(REFRESH, false)) {
            this.viewModel.refresh()
            this.arguments!!.putBoolean(REFRESH, false)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        this.binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_years, container, false
        )
        this.viewModel.years(LifecycleCallback(this) { r ->
            if (r.isSuccess()) {
                draw(r.value())
            } else {
                ErrorDialog.new(r.exception()).show(this.childFragmentManager)
            }
        })
        this.root.changeTitle(getString(R.string.years))
        return this.binding.root
    }

    private fun draw(years: List<Int>) {
        if (years.isEmpty()) {
            this.binding.noHistory.visibility = View.VISIBLE
            this.binding.ok.visibility = View.VISIBLE
            this.binding.ok.setOnClickListener { requireFragmentManager().popBackStack() }
        } else {
            this.binding.years.layoutManager = GridOrLinear(requireContext(), binding.years).value()
            this.binding.years.adapter = YearsView(years, this)
        }
    }

    override fun hit(item: Int) {
        this.root.replace(MonthsFragment.new(item), true)
    }

    override fun hit(message: Message) {
        if (message == Message.DAYS_CHANGED) {
            if (this::viewModel.isInitialized) {
                this.viewModel.refresh()
            } else {
                this.arguments!!.putBoolean(REFRESH, true)
            }
        }
    }
}