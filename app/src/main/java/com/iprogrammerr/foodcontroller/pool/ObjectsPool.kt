package com.iprogrammerr.foodcontroller.pool

object ObjectsPool {

    private val pool = HashMap<Class<*>, Any>()

    fun add(clazz: Class<*>, any: Any) {
        if (this.pool.containsKey(clazz)) {
            throw Exception("Pool already contains class of $clazz type")
        }
        this.pool[clazz] = any
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> single(clazz: Class<T>): T {
        val item = this.pool[clazz]
        if (item == null || !clazz.isAssignableFrom(item.javaClass)) {
            throw Exception("Pool does not contain class of $clazz type")
        }
        return item as T
    }
}