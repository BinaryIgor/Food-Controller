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
import com.iprogrammerr.foodcontroller.model.category.Category
import com.iprogrammerr.foodcontroller.model.result.LifecycleCallback
import com.iprogrammerr.foodcontroller.view.RootView
import com.iprogrammerr.foodcontroller.view.items.CategoriesSelectableView
import com.iprogrammerr.foodcontroller.view.message.Message
import com.iprogrammerr.foodcontroller.viewmodel.MoveDeleteFoodViewModel

class MoveDeleteFoodDialog : DialogFragment() {

    private lateinit var root: RootView
    private val viewModel by lazy {
        ViewModelProviders.of(this).get(MoveDeleteFoodViewModel::class.java)
    }

    companion object {

        private const val ID = "ID"
        private const val TITLE = "TITLE"
        private const val CATEGORY_ID = "CATEGORY_ID"

        fun new(id: Long, title: String): MoveDeleteFoodDialog {
            val fragment = MoveDeleteFoodDialog()
            val args = Bundle()
            args.putLong(ID, id)
            args.putString(TITLE, title)
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
        val binding: DialogMoveDeleteBinding = DataBindingUtil.inflate(
            LayoutInflater.from(this.context),
            R.layout.dialog_move_delete,
            null,
            false
        )
        val args = this.arguments as Bundle
        binding.title.text = args.getString(TITLE)
        this.viewModel.categories(LifecycleCallback(this) { r ->
            if (r.isSuccess()) {
                setupSpinner(binding.categories, r.value())
            } else {
                Snackbar.make(binding.root, r.exception(), Snackbar.LENGTH_LONG).show()
            }
        })
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

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int,
                id: Long) {
                arguments?.putLong(CATEGORY_ID, items[position].id())
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    private fun moveFood(root: View) {
        val id = this.arguments!!.getLong(CATEGORY_ID, -1)
        if (id < 0) {
            Snackbar.make(root, getString(R.string.choose_category), Snackbar.LENGTH_LONG).show()
        } else {
            this.viewModel.move(id, this.arguments!!.getLong(ID, -1),
                LifecycleCallback(this) { r ->
                    if (r.isSuccess()) {
                        dismiss()
                        this.root.propagate(Message.FOOD_DEFINITION_MOVED)
                    } else {
                        Snackbar.make(root, r.exception(), Snackbar.LENGTH_LONG).show()
                    }
                }
            )
        }
    }

    private fun deleteFood(root: View) {
        this.viewModel.delete(this.arguments!!.getLong(ID),
            LifecycleCallback(this) { r ->
                if (r.isSuccess()) {
                    dismiss()
                    this.root.propagate(Message.FOOD_DEFINITION_CHANGED)
                } else {
                    Snackbar.make(root, r.exception(), Snackbar.LENGTH_LONG).show()
                }
            }
        )
    }

    fun show(manager: FragmentManager) {
        if (manager.findFragmentByTag(tag()) == null) {
            show(manager, tag())
        }
    }
}