package com.iprogrammerr.foodcontroller.pool

import org.junit.Assert.assertThat
import org.junit.Test

class ObjectsPoolTest {

    @Test
    fun addAndRetrieve() {
        assertThat(ObjectsPool, ObjectsPoolThatCanAddAndRetrieve(1))
    }

    @Test(expected = Exception::class)
    fun doesNotHaveDuplicates() {
        val item = "item"
        ObjectsPool.add(item)
        ObjectsPool.add(item)
    }
}