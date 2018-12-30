package com.iprogrammerr.foodcontroller.view.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentManager
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.Spinner
import com.iprogrammerr.foodcontroller.R
import com.iprogrammerr.foodcontroller.databinding.DialogMoveDeleteBinding
import com.iprogrammerr.foodcontroller.model.category.Categories
import com.iprogrammerr.foodcontroller.model.category.Category
import com.iprogrammerr.foodcontroller.model.food.FoodDefinitions
import com.iprogrammerr.foodcontroller.pool.ObjectsPool
import com.iprogrammerr.foodcontroller.view.RootView
import com.iprogrammerr.foodcontroller.view.items.CategoriesSelectableView
import com.iprogrammerr.foodcontroller.view.message.Message
import com.iprogrammerr.foodcontroller.viewmodel.MoveDeleteFoodViewModel
import com.iprogrammerr.foodcontroller.viewmodel.factory.MoveDeleteFoodViewModelFactory
import java.util.concurrent.Executor

class MoveDeleteFoodDialog : DialogFragment() {

    private lateinit var root: RootView
    private val viewModel by lazy {
        ViewModelProviders.of(
            this, MoveDeleteFoodViewModelFactory(
                ObjectsPool.single(Executor::class.java), ObjectsPool.single(Categories::class.java),
                ObjectsPool.single(FoodDefinitions::class.java)
            )
        ).get(MoveDeleteFoodViewModel::class.java)
    }

    companion object {

        fun new(id: Long, title: String): MoveDeleteFoodDialog {
            val fragment = MoveDeleteFoodDialog()
            val args = Bundle()
            args.putLong("id", id)
            args.putString("title", title)
            fragment.arguments = args
            return fragment
        }

        fun tag() = this::class.java.simpleName as String
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        this.root = context as RootView
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = AlertDialog.Builder(this.context).create()
        val binding: DialogMoveDeleteBinding =
            DataBindingUtil.inflate(LayoutInflater.from(this.context), R.layout.dialog_move_delete, null, false)
        val args = this.arguments as Bundle
        binding.title.text = args.getString("title")
        this.viewModel.categories { r ->
            if (r.isSuccess()) {
                this.root.runOnMain { setupSpinner(binding.categories, r.value()) }
            } else {
                Snackbar.make(binding.root, r.exception(), Snackbar.LENGTH_LONG).show()
            }
        }
        binding.move.setOnClickListener { moveFood(binding.root) }
        binding.delete.setOnClickListener { deleteFood(binding.root) }
        dialog.setView(binding.root)
        return dialog
    }

    private fun setupSpinner(spinner: Spinner, categories: List<Category>) {
        val items: MutableList<Category> = ArrayList(categories.size + 1)
        items.add(Category.Fake(getString(R.string.choose_category)))
        items.addAll(categories)
        spinner.adapter = CategoriesSelectableView(this.context as Context, items)
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                arguments?.putLong("category_id", items[position].id())
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    private fun moveFood(root: View) {
        val id = this.arguments!!.getLong("category_id", -1)
        if (id < 0) {
            Snackbar.make(root, getString(R.string.choose_category), Snackbar.LENGTH_LONG).show()
        } else {
            this.viewModel.move(id, this.arguments!!.getLong("id", -1)) { r ->
                if (r.isSuccess()) {
                    dismiss()
                    this.root.propagate(Message.FoodDefinitionMoved)
                } else {
                    Snackbar.make(root, r.exception(), Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun deleteFood(root: View) {
        this.viewModel.delete(this.arguments!!.getLong("id")) { r ->
            if (r.isSuccess()) {
                dismiss()
                this.root.propagate(Message.FoodDefinitionsChanged)
            } else {
                Snackbar.make(root, r.exception(), Snackbar.LENGTH_LONG).show()
            }
        }
    }

    fun show(manager: FragmentManager) {
        if (manager.findFragmentByTag(tag()) == null) {
            show(manager, tag())
        }
    }
}