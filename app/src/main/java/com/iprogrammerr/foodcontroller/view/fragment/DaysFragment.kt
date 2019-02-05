package com.iprogrammerr.foodcontroller.view.fragment

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.iprogrammerr.foodcontroller.ObjectsPool
import com.iprogrammerr.foodcontroller.R
import com.iprogrammerr.foodcontroller.databinding.FragmentDaysBinding
import com.iprogrammerr.foodcontroller.model.NutritionalValues
import com.iprogrammerr.foodcontroller.model.format.Formats
import com.iprogrammerr.foodcontroller.model.result.LifecycleCallback
import com.iprogrammerr.foodcontroller.model.scalar.GridOrLinear
import com.iprogrammerr.foodcontroller.view.RootView
import com.iprogrammerr.foodcontroller.view.dialog.ErrorDialog
import com.iprogrammerr.foodcontroller.view.items.DateTarget
import com.iprogrammerr.foodcontroller.view.items.DaysView
import com.iprogrammerr.foodcontroller.view.message.Message
import com.iprogrammerr.foodcontroller.view.message.MessageTarget
import com.iprogrammerr.foodcontroller.viewmodel.DaysViewModel
import java.util.*
import kotlin.math.roundToInt

class DaysFragment : Fragment(), DateTarget, MessageTarget {

    private lateinit var root: RootView
    private val format = ObjectsPool.single(Formats::class.java).date()
    private lateinit var viewModel: DaysViewModel

    companion object {

        private const val MONTH = "MONTH"
        private const val MONTH_VALUE = "MONTH_VALUE"

        fun new(month: Calendar): DaysFragment {
            val fragment = DaysFragment()
            val args = Bundle()
            args.putLong(MONTH, month.timeInMillis)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        this.root = context as RootView
        val month = Calendar.getInstance()
        month.timeInMillis = this.arguments!!.getLong(MONTH)
        this.arguments!!.putString(
            MONTH_VALUE,
            this.context!!.resources.getStringArray(R.array.months)[month[Calendar.MONTH]]
        )
        this.viewModel = ViewModelProviders.of(
            this,
            DaysViewModel.factory(month)
        ).get(DaysViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        val binding: FragmentDaysBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_days, container, false
        )
        this.viewModel.statistics(LifecycleCallback(this) { r ->
            if (r.isSuccess()) {
                drawStatistics(binding, r.value())
            } else {
                ErrorDialog.new(r.exception()).show(this.childFragmentManager)
            }
        })
        this.viewModel.days(LifecycleCallback(this) { r ->
            if (r.isSuccess()) {
                if (r.value().isEmpty()) {
                    requireFragmentManager().popBackStack()
                } else {
                    binding.days.layoutManager = GridOrLinear(
                        requireContext(), binding.days
                    ).value()
                    binding.days.adapter = DaysView(this.format, r.value(), this)
                }
            } else {
                ErrorDialog.new(r.exception()).show(this.childFragmentManager)
            }
        })
        this.root.changeTitle(this.arguments!!.getString(MONTH_VALUE, ""))
        return binding.root
    }

    private fun drawStatistics(binding: FragmentDaysBinding,
        statistics: Pair<NutritionalValues, NutritionalValues>) {
        val achieved = statistics.first
        val goals = statistics.second
        binding.caloriesValue.text = "${achieved.calories()}/${goals.calories()}"
        binding.caloriesProgress.max = goals.calories()
        binding.caloriesProgress.progress = achieved.calories()
        binding.proteinValue.text = "${achieved.protein().roundToInt()}/${goals.protein().roundToInt()}"
        binding.proteinProgress.max = goals.protein().roundToInt()
        binding.proteinProgress.progress = achieved.protein().roundToInt()
    }

    override fun hit(date: Long) {
        this.root.replace(DayFragment.new(date), true)
    }

    override fun hit(message: Message) {
        if (this::viewModel.isInitialized && (message == Message.DAYS_CHANGED || message == Message.MEALS_CHANGED)) {
            this.viewModel.refresh()
        }
    }
}