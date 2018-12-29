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
import com.iprogrammerr.foodcontroller.database.Database
import com.iprogrammerr.foodcontroller.databinding.FragmentCategoryFoodDefinitionsBinding
import com.iprogrammerr.foodcontroller.model.IdTarget
import com.iprogrammerr.foodcontroller.model.category.DatabaseCategory
import com.iprogrammerr.foodcontroller.model.food.FoodDefinition
import com.iprogrammerr.foodcontroller.model.result.Result
import com.iprogrammerr.foodcontroller.pool.ObjectsPool
import com.iprogrammerr.foodcontroller.view.RootView
import com.iprogrammerr.foodcontroller.view.dialog.InformationDialog
import com.iprogrammerr.foodcontroller.view.items.CategoryProductsView
import com.iprogrammerr.foodcontroller.viewmodel.CategoryFoodDefinitionsViewModel
import com.iprogrammerr.foodcontroller.viewmodel.factory.CategoryFoodDefinitionsViewModelFactory
import java.util.concurrent.Executor

class CategoryFoodDefinitionsFragment : Fragment(), TextWatcher, IdTarget {

    private lateinit var root: RootView
    private lateinit var binding: FragmentCategoryFoodDefinitionsBinding
    private lateinit var products: CategoryProductsView
    private val viewModel by lazy {
        ViewModelProviders.of(
            this,
            CategoryFoodDefinitionsViewModelFactory(
                ObjectsPool.single(Executor::class.java),
                DatabaseCategory(this.arguments!!.getLong("id"), ObjectsPool.single(Database::class.java))
            )
        ).get(CategoryFoodDefinitionsViewModel::class.java)
    }

    companion object {

        fun new(id: Long): CategoryFoodDefinitionsFragment {
            val fragment = CategoryFoodDefinitionsFragment()
            val args = Bundle()
            args.putLong("id", id)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        this.root = context as RootView
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.binding =
                DataBindingUtil.inflate(inflater, R.layout.fragment_category_food_definitions, container, false)
        this.binding.products.layoutManager = LinearLayoutManager(this.context)
        val criteria = this.arguments!!.getString("criteria", "")
        if (criteria.isBlank()) {
            this.viewModel.products { r -> drawListOrDialog(r) }
        } else {
            this.viewModel.filtered(criteria) { r -> drawListOrDialog(r) }
        }
        this.viewModel.name { r ->
            if (r.isSuccess()) {
                this.root.changeTitle(r.value())
            } else {
                InformationDialog.new(r.exception()).show(this.childFragmentManager)
            }
        }
        this.binding.searchInput.addTextChangedListener(this)
        return this.binding.root
    }

    private fun drawListOrDialog(result: Result<List<FoodDefinition>>) {
        this.root.runOnMain {
            if (result.isSuccess()) {
                if (this::products.isInitialized) {
                    this.products.refresh(result.value())
                } else {
                    this.products = CategoryProductsView(result.value(), this)
                }
                this.binding.products.adapter = this.products
            } else {
                InformationDialog.new(result.exception()).show(this.childFragmentManager)
            }
        }
    }

    override fun afterTextChanged(s: Editable?) {

    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        s?.let { s ->
            val criteria = s.toString()
            this.arguments!!.putString("criteria", criteria)
            if (criteria.isNotBlank()) {
                this.viewModel.filtered(criteria) { r -> drawListOrDialog(r) }
            } else {
                this.viewModel.products { r -> drawListOrDialog(r) }
            }
        }
    }

    override fun hit(id: Long) {
        this.root.replace(FoodDefinitionFragment.withId(id), true)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        this.binding.searchInput.removeTextChangedListener(this)
    }
}