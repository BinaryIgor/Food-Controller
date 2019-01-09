package com.iprogrammerr.foodcontroller.view.fragment

import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.iprogrammerr.foodcontroller.R
import com.iprogrammerr.foodcontroller.databinding.FragmentFoodBinding
import com.iprogrammerr.foodcontroller.model.IdTarget
import com.iprogrammerr.foodcontroller.model.food.FoodDefinition
import com.iprogrammerr.foodcontroller.model.result.LifecycleCallback
import com.iprogrammerr.foodcontroller.model.result.Result
import com.iprogrammerr.foodcontroller.view.RootView
import com.iprogrammerr.foodcontroller.view.dialog.ErrorDialog
import com.iprogrammerr.foodcontroller.view.items.FoodDefinitionsView
import com.iprogrammerr.foodcontroller.view.message.Message
import com.iprogrammerr.foodcontroller.view.message.MessageTarget
import com.iprogrammerr.foodcontroller.viewmodel.FoodViewModel

class FoodFragment : Fragment(), TextWatcher, IdTarget, MessageTarget {

    private lateinit var root: RootView
    private lateinit var binding: FragmentFoodBinding
    private lateinit var food: FoodDefinitionsView
    private val viewModel by lazy {
        ViewModelProviders.of(this).get(FoodViewModel::class.java)
    }

    companion object {
        fun new(mealId: Long): FoodFragment {
            val fragment = FoodFragment()
            val args = Bundle()
            args.putLong("mealId", mealId)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        this.root = context as RootView
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_food,
            container, false
        )
        this.binding.add.setOnClickListener {
            this.root.replace(FoodDefinitionFragment.withCategories(), true)
        }
        this.binding.food.layoutManager = LinearLayoutManager(this.context)
        drawAllOrFiltered()
        this.binding.searchInput.addTextChangedListener(this)
        this.root.changeTitle(getString(R.string.choose_food))
        return this.binding.root
    }

    private fun drawAllOrFiltered() {
        val criteria = this.arguments!!.getString("criteria", "")
        if (criteria.isBlank()) {
            this.viewModel.all(
                LifecycleCallback(this) { r -> drawListOrDialog(r) })
        } else {
            this.viewModel.filtered(criteria,
                LifecycleCallback(this) { r -> drawListOrDialog(r) })
        }
    }

    private fun drawListOrDialog(result: Result<List<FoodDefinition>>) {
        if (result.isSuccess()) {
            if (this::food.isInitialized) {
                this.food.refresh(result.value())
            } else {
                this.food = FoodDefinitionsView(result.value(), this)
            }
            this.binding.food.adapter = this.food
        } else {
            ErrorDialog.new(result.exception()).show(this.childFragmentManager)
        }
    }

    override fun afterTextChanged(s: Editable?) {

    }

    override fun beforeTextChanged(
        s: CharSequence?, start: Int, count: Int,
        after: Int
    ) {

    }

    override fun onTextChanged(
        s: CharSequence?, start: Int, before: Int,
        count: Int
    ) {
        s?.let { s ->
            val criteria = s.toString()
            val args = this.arguments as Bundle
            if (args.getString("criteria", "") != criteria) {
                args.putString("criteria", criteria)
                drawAllOrFiltered()
            }
        }
    }

    override fun hit(id: Long) {
        this.root.replace(
            FoodPortionFragment.new(
                id,
                (this.arguments as Bundle).getLong("mealId")
            ), true
        )

    }

    override fun hit(message: Message) {
        if (message == Message.FOOD_DEFINITION_CHANGED) {
            this.viewModel.refresh()
        }
    }

    override fun isInterested(message: Message) = message == Message.FOOD_DEFINITION_CHANGED

    override fun onDestroyView() {
        super.onDestroyView()
        this.binding.searchInput.removeTextChangedListener(this)
    }
}