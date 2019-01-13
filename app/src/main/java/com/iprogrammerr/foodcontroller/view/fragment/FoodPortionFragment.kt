package com.iprogrammerr.foodcontroller.view.fragment

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.iprogrammerr.foodcontroller.R
import com.iprogrammerr.foodcontroller.databinding.FragmentFoodPortionBinding
import com.iprogrammerr.foodcontroller.model.NutritionalValues
import com.iprogrammerr.foodcontroller.model.NutritionalValuesFromView
import com.iprogrammerr.foodcontroller.model.result.LifecycleCallback
import com.iprogrammerr.foodcontroller.model.scalar.IntFromView
import com.iprogrammerr.foodcontroller.view.RootView
import com.iprogrammerr.foodcontroller.view.dialog.ErrorDialog
import com.iprogrammerr.foodcontroller.view.dialog.InformationDialog
import com.iprogrammerr.foodcontroller.view.message.Message
import com.iprogrammerr.foodcontroller.viewmodel.FoodPortionViewModel

class FoodPortionFragment : Fragment(), TextWatcher {

    private lateinit var root: RootView
    private lateinit var binding: FragmentFoodPortionBinding
    private lateinit var weight: IntFromView
    private lateinit var values: NutritionalValues
    private val viewModel by lazy {
        ViewModelProviders.of(this, FoodPortionViewModel.factory(this.arguments!!.getLong("id")))
            .get(FoodPortionViewModel::class.java)
    }

    companion object {
        fun new(id: Long, mealId: Long) =
            withWeight(id, mealId, 100)

        fun withWeight(id: Long, mealId: Long, weight: Int): FoodPortionFragment {
            val fragment = FoodPortionFragment()
            val args = Bundle()
            args.putLong("id", id)
            args.putLong("mealId", mealId)
            args.putInt("weight", weight)
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
        this.binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_food_portion, container, false
        )
        val args = this.arguments as Bundle
        this.binding.weight.addTextChangedListener(this)
        this.weight = IntFromView(this.binding.weight, 1)
        this.viewModel.definition(LifecycleCallback(this) { r ->
            if (r.isSuccess()) {
                this.values = NutritionalValuesFromView(r.value().calories(), r.value().protein(),
                    this.weight)
                this.binding.weight.setText(args.getInt("weight").toString())
                this.root.changeTitle(r.value().name())
            } else {
                ErrorDialog.new(r.exception()).show(this.childFragmentManager)
            }
        })
        this.binding.save.setOnClickListener { save() }
        return this.binding.root
    }

    private fun save() {
        if (this.weight.value() < 1) {
            InformationDialog.new(getString(R.string.weight_invalid))
                .show(this.childFragmentManager)
        } else {
            this.viewModel.add(this.weight.value(), this.arguments!!.getLong("mealId"),
                LifecycleCallback(this) { r ->
                    if (r.isSuccess()) {
                        this.root.propagate(Message.PORTION_ADDED)
                    } else {
                        ErrorDialog.new(r.exception()).show(this.childFragmentManager)
                    }
                })
        }
    }

    override fun afterTextChanged(s: Editable?) {

    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int,
        after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int,
        count: Int) {
        if (this::values.isInitialized) {
            this.binding.calories.text = this.values.calories().toString()
            this.binding.protein.text = this.values.protein().toString()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        this.binding.weight.removeTextChangedListener(this)
        this.arguments?.putInt("weight", this.weight.value())
    }
}