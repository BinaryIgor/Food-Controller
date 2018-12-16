package com.iprogrammerr.foodcontroller.pool

object ObjectsPool {

    private val pool = HashMap<Class<*>, Any>()

    fun add(any: Any) {
        if (this.pool.containsKey(any.javaClass)) {
            throw Exception("Pool already contains class of ${any.javaClass} type")
        }
        this.pool[any.javaClass] = any
    }

    fun <T> single(clazz: Class<T>): T {
        val item = this.pool[clazz]
        if (item == null || !item.javaClass.isAssignableFrom(clazz)) {
            throw Exception("Pool does not contain class of ${clazz} type")
        }
        return clazz.cast(item)
    }
}