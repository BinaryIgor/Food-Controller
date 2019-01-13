package com.iprogrammerr.foodcontroller.pool

import com.iprogrammerr.foodcontroller.ObjectsPool
import org.junit.Assert.assertThat
import org.junit.Test

class ObjectsPoolTest {

    @Test
    fun addAndRetrieve() {
        assertThat(ObjectsPool, ObjectsPoolThatCanAddAndRetrieve(Any::class.java, 1))
    }

    @Test(expected = Exception::class)
    fun doesNotHaveDuplicates() {
        val item = "item"
        ObjectsPool.add(String::class.java, item)
        ObjectsPool.add(String::class.java, item)
    }
}