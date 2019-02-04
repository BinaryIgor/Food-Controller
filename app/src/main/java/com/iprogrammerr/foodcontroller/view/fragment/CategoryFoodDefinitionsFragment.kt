package com.iprogrammerr.foodcontroller.view.fragment

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.iprogrammerr.foodcontroller.ObjectsPool
import com.iprogrammerr.foodcontroller.R
import com.iprogrammerr.foodcontroller.database.Database
import com.iprogrammerr.foodcontroller.databinding.FragmentCategoryFoodDefinitionsBinding
import com.iprogrammerr.foodcontroller.model.IdTarget
import com.iprogrammerr.foodcontroller.model.category.DatabaseCategory
import com.iprogrammerr.foodcontroller.model.food.FoodDefinition
import com.iprogrammerr.foodcontroller.model.result.LifecycleCallback
import com.iprogrammerr.foodcontroller.model.result.Result
import com.iprogrammerr.foodcontroller.model.scalar.GridOrLinear
import com.iprogrammerr.foodcontroller.view.RootView
import com.iprogrammerr.foodcontroller.view.dialog.ErrorDialog
import com.iprogrammerr.foodcontroller.view.dialog.MoveDeleteFoodDialog
import com.iprogrammerr.foodcontroller.view.items.AdapterTarget
import com.iprogrammerr.foodcontroller.view.items.FoodDefinitionsView
import com.iprogrammerr.foodcontroller.view.message.Message
import com.iprogrammerr.foodcontroller.view.message.MessageTarget
import com.iprogrammerr.foodcontroller.viewmodel.CategoryFoodDefinitionsViewModel

class CategoryFoodDefinitionsFragment : Fragment(), TextWatcher, IdTarget, AdapterTarget<FoodDefinition>,
    MessageTarget {

    private lateinit var root: RootView
    private lateinit var binding: FragmentCategoryFoodDefinitionsBinding
    private lateinit var food: FoodDefinitionsView
    private val id by lazy {
        this.arguments?.let { it.getLong(ID, -1) } ?: -1
    }
    private val viewModel by lazy {
        ViewModelProviders.of(
            this,
            CategoryFoodDefinitionsViewModel.factory(
                DatabaseCategory(this.id, ObjectsPool.single(Database::class.java))
            )
        ).get(CategoryFoodDefinitionsViewModel::class.java)
    }

    companion object {

        private const val ID = "ID"
        private const val CRITERIA = "CRITERIA"

        fun new(id: Long): CategoryFoodDefinitionsFragment {
            val fragment = CategoryFoodDefinitionsFragment()
            val args = Bundle()
            args.putLong(ID, id)
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
            inflater, R.layout.fragment_category_food_definitions, container, false
        )
        this.binding.food.layoutManager = GridOrLinear(requireContext(), this.binding.food).value()
        drawAllOrFiltered()
        this.binding.add.setOnClickListener {
            this.root.replace(FoodDefinitionFragment.withCategoryId(this.id), true)
        }
        this.viewModel.name(LifecycleCallback(this) { r ->
            if (r.isSuccess()) {
                this.root.changeTitle(r.value())
            } else {
                ErrorDialog.new(r.exception()).show(this.childFragmentManager)
            }
        })
        this.binding.searchInput.addTextChangedListener(this)
        return this.binding.root
    }

    private fun drawAllOrFiltered() {
        val criteria = this.arguments!!.getString(CRITERIA, "")
        if (criteria.isBlank()) {
            this.viewModel.all(LifecycleCallback(this) { r -> drawListOrDialog(r) })
        } else {
            this.viewModel.filtered(criteria, LifecycleCallback(this) { r -> drawListOrDialog(r) })
        }
    }

    private fun drawListOrDialog(result: Result<List<FoodDefinition>>) {
        if (result.isSuccess()) {
            if (this::food.isInitialized) {
                this.food.refresh(result.value())
            } else {
                this.food = FoodDefinitionsView(result.value(), this, this)
            }
            this.binding.food.adapter = this.food
        } else {
            ErrorDialog.new(result.exception()).show(this.childFragmentManager)
        }
    }

    override fun afterTextChanged(s: Editable?) {

    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        s?.let {
            val criteria = s.toString()
            val args = this.arguments as Bundle
            if (args.getString(CRITERIA, "") != criteria) {
                args.putString(CRITERIA, criteria)
                drawAllOrFiltered()
            }
        }
    }

    override fun hit(id: Long) {
        this.root.replace(FoodDefinitionFragment.withId(id), true)
    }

    override fun hit(item: FoodDefinition) {
        MoveDeleteFoodDialog.new(item.id(), item.name()).show(
            this.fragmentManager as FragmentManager)
    }

    override fun hit(message: Message) {
        if (message == Message.FOOD_DEFINITION_CHANGED || message == Message.FOOD_DEFINITION_MOVED) {
            this.viewModel.refresh()
            if (this.lifecycle.currentState.isAtLeast(Lifecycle.State.CREATED)) {
                drawAllOrFiltered()
                if (message == Message.FOOD_DEFINITION_MOVED) {
                    Snackbar.make(this.binding.root, getString(R.string.definition_moved),
                        Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        this.binding.searchInput.removeTextChangedListener(this)
    }
}