package com.iprogrammerr.foodcontroller.livedata

import android.arch.lifecycle.MutableLiveData
import com.iprogrammerr.foodcontroller.model.result.Result

class ResultLiveData<T> : MutableLiveData<Result<T>>() {

    fun isEmptyOrFailure() = this.value == null || this.value!!.isSuccess()
}