package com.iprogrammerr.foodcontroller

import org.junit.Test
import java.util.*

class QuickTest {

    @Test
    fun query() {
        val map = HashMap<String, Any>()
        map["name"] = "Igor"
        if (map["id"] is Long) {
            System.out.println("There is a long!")
        } else {
            System.out.println("There is no any long!")
        }
    }
}