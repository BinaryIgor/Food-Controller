package com.iprogrammerr.foodcontroller.view.fragment

import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.iprogrammerr.foodcontroller.R
import com.iprogrammerr.foodcontroller.databinding.FragmentProductDefinitionBinding
import com.iprogrammerr.foodcontroller.view.RootView
import com.iprogrammerr.foodcontroller.view.dialog.InformationDialog

class ProductDefinitionFragment : Fragment() {

    private lateinit var root: RootView
    private lateinit var binding: FragmentProductDefinitionBinding
    private val id by lazy {
        this.arguments?.let { it.getLong("id", -1) } ?: -1L
    }

    companion object {

        fun new(id: Long): ProductDefinitionFragment {
            val fragment = ProductDefinitionFragment()
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
        this.binding = DataBindingUtil.inflate(inflater, R.layout.fragment_product_definition, container, false)
        this.binding.save.setOnClickListener { save() }
        return this.binding.root
    }

    private fun save() {
        val name = this.binding.nameInput.text.toString()
        val calories = calories()
        val protein = protein()
        when {
            name.length < 3 -> InformationDialog.new(getString(R.string.name_invalid)).show(this.childFragmentManager)
            calories < 1 -> InformationDialog.new(getString(R.string.calories_invalid)).show(this.childFragmentManager)
            protein < 0 -> InformationDialog.new(getString(R.string.protein_invalid)).show(this.childFragmentManager)
            //TODO call view model and save
        }
    }

    private fun calories(): Int {
        return try {
            this.binding.caloriesInput.text.toString().trim().toInt()
        } catch (e: Exception) {
            0
        }
    }

    private fun protein(): Double {
        return try {
            this.binding.proteinInput.text.toString().trim().toDouble()
        } catch (e: Exception) {
            -1.0
        }
    }
}