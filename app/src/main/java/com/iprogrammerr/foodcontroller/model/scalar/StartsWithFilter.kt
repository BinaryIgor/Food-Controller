package com.iprogrammerr.foodcontroller.model.scalar

import com.iprogrammerr.foodcontroller.model.Named

class StartsWithFilter<T : Named>(
    private val items: List<T>,
    private val criteria: String,
    private val ignoreCase: Boolean
) : Scalar<List<T>> {

    constructor(items: List<T>, criteria: String) : this(items, criteria, true)

    override fun value(): List<T> {
        val filtered: MutableList<T> = ArrayList()
        for (i in this.items) {
            if (i.name().startsWith(this.criteria, this.ignoreCase)) {
                filtered.add(i)
            }
        }
        return filtered
    }
}