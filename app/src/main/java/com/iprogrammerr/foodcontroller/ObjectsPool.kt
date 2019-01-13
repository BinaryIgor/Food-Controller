package com.iprogrammerr.foodcontroller

object ObjectsPool {

    private val pool = HashMap<Class<*>, Any>()

    fun add(clazz: Class<*>, any: Any) {
        if (pool.containsKey(clazz)) {
            throw Exception("Pool already contains class of $clazz type")
        }
        pool[clazz] = any
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> single(clazz: Class<T>): T {
        val item = pool[clazz]
        if (item == null || !clazz.isAssignableFrom(item.javaClass)) {
            throw Exception("Pool does not contain class of $clazz type")
        }
        return item as T
    }
}