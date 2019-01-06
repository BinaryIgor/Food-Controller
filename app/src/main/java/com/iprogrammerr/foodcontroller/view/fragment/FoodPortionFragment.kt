package com.iprogrammerr.foodcontroller.view.fragment

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
import com.iprogrammerr.foodcontroller.model.scalar.IntFromView
import com.iprogrammerr.foodcontroller.view.RootView

class FoodPortionFragment : Fragment(), TextWatcher {

    private lateinit var root: RootView
    private lateinit var binding: FragmentFoodPortionBinding
    private lateinit var weight: IntFromView

    companion object {
        fun new(id: Long, mealId: Long): FoodPortionFragment {
            val fragment = FoodPortionFragment()
            val args = Bundle()
            args.putLong("id", id)
            args.putLong("mealId", mealId)
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
        if (args.getInt("weight", -1) != -1) {
            this.binding.weight.setText(args.getInt("weight").toString())
        }
        this.binding.weight.addTextChangedListener(this)
        this.weight = IntFromView(this.binding.weight)
        return this.binding.root
    }

    override fun afterTextChanged(s: Editable?) {

    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int,
            after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int,
            count: Int) {
        s?.let {

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        this.binding.weight.removeTextChangedListener(this)
        this.arguments!!.putInt("weight", this.weight.value())
    }
}