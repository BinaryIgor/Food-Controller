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
import com.iprogrammerr.foodcontroller.databinding.FragmentFoodDefinitionBinding
import com.iprogrammerr.foodcontroller.model.DoubleFromView
import com.iprogrammerr.foodcontroller.model.IntFromView
import com.iprogrammerr.foodcontroller.model.result.LifecycleCallback
import com.iprogrammerr.foodcontroller.model.result.Result
import com.iprogrammerr.foodcontroller.view.RootView
import com.iprogrammerr.foodcontroller.view.dialog.ErrorDialog
import com.iprogrammerr.foodcontroller.view.dialog.InformationDialog
import com.iprogrammerr.foodcontroller.view.message.Message
import com.iprogrammerr.foodcontroller.viewmodel.FoodDefinitionViewModel

class FoodDefinitionFragment : Fragment() {

    private lateinit var root: RootView
    private lateinit var binding: FragmentFoodDefinitionBinding
    private val viewModel by lazy {
        ViewModelProviders.of(this).get(FoodDefinitionViewModel::class.java)
    }

    companion object {
        fun withId(id: Long): FoodDefinitionFragment {
            val fragment = FoodDefinitionFragment()
            val args = Bundle()
            args.putLong("id", id)
            fragment.arguments = args
            return fragment
        }

        fun withCategoryId(id: Long): FoodDefinitionFragment {
            val fragment = FoodDefinitionFragment()
            val args = Bundle()
            args.putLong("categoryId", id)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        this.root = context as RootView
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.binding = DataBindingUtil.inflate(inflater, R.layout.fragment_food_definition, container, false)
        val args = this.arguments as Bundle
        if (args.getLong("id", -1) > 0) {
            initInputs(args.getLong("id"))
            this.root.changeTitle(getString(R.string.edit_definition))
        } else {
            this.root.changeTitle(getString(R.string.add_definition))
        }
        this.binding.save.setOnClickListener { save() }
        return this.binding.root
    }

    private fun initInputs(id: Long) {
        this.viewModel.definition(id, LifecycleCallback(this) { r ->
            if (r.isSuccess()) {
                this.binding.nameInput.setText(r.value().name())
                this.binding.caloriesInput.setText(r.value().calories().toString())
                this.binding.proteinInput.setText(r.value().protein().toString())
            } else {
                InformationDialog.new(r.exception()).show(this.childFragmentManager)
            }
        })
    }

    private fun save() {
        val name = this.binding.nameInput.text.toString()
        val calories = IntFromView(this.binding.caloriesInput).value()
        val protein = DoubleFromView(this.binding.proteinInput).value()
        val args = this.arguments as Bundle
        when {
            name.length < 3 -> InformationDialog.new(getString(R.string.name_invalid)).show(this.childFragmentManager)
            calories < 1 -> InformationDialog.new(getString(R.string.calories_invalid)).show(this.childFragmentManager)
            protein < 0 -> InformationDialog.new(getString(R.string.protein_invalid)).show(this.childFragmentManager)
            args.getLong("id", -1) > 0 ->
                this.viewModel.update(args.getLong("id"), name, calories, protein,
                    LifecycleCallback(this) { r -> onSaveResult(r) })
            else ->
                this.viewModel.add(name, calories, protein, args.getLong("categoryId"),
                    LifecycleCallback(this) { r -> onSaveResult(r) })
        }
    }

    private fun onSaveResult(result: Result<Boolean>) {
        if (result.isSuccess()) {
            this.root.propagate(Message.FoodDefinitionsChanged)
            this.fragmentManager?.popBackStack()
        } else {
            ErrorDialog.new(result.exception()).show(this.childFragmentManager)
        }
    }
}