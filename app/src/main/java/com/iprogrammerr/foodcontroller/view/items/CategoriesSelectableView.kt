package com.iprogrammerr.foodcontroller.view.items

import android.content.Context
import android.databinding.DataBindingUtil
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.iprogrammerr.foodcontroller.R
import com.iprogrammerr.foodcontroller.databinding.ItemSpinnerBinding
import com.iprogrammerr.foodcontroller.databinding.ItemSpinnerDropdownBinding
import com.iprogrammerr.foodcontroller.model.category.Category

class CategoriesSelectableView(
    context: Context,
    private val enabledFrom: Int,
    categories: List<Category>
) : ArrayAdapter<Category>(context, 0, categories) {

    constructor(context: Context, categories: List<Category>) : this(context, 1, categories)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding = convertView?.let {
            DataBindingUtil.getBinding<ItemSpinnerBinding>(convertView)
        }
            ?: DataBindingUtil.inflate(
                LayoutInflater.from(this.context), R.layout.item_spinner, parent, false
            )
        binding.description.text = getItem(position)!!.name()
        return binding.root
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding = convertView?.let {
            DataBindingUtil.getBinding<ItemSpinnerDropdownBinding>(convertView)
        }
            ?: DataBindingUtil.inflate(
                LayoutInflater.from(this.context), R.layout.item_spinner_dropdown, parent, false
            )
        binding.description.text = getItem(position)!!.name()
        return binding.root
    }

    override fun isEnabled(position: Int) = position >= this.enabledFrom
}