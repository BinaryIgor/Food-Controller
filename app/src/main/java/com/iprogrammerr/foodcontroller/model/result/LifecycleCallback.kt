package com.iprogrammerr.foodcontroller.model.result

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.OnLifecycleEvent
import com.iprogrammerr.foodcontroller.model.Potential

class LifecycleCallback<T>(private val owner: LifecycleOwner, private val callback: (Result<T>) -> Unit) : Callback<T>,
    LifecycleObserver {

    private val last: Potential<Result<T>> = Potential()

    override fun call(result: Result<T>) {
        if (this.owner.lifecycle.currentState.isAtLeast(Lifecycle.State.CREATED)) {
            this.callback(result)
        } else {
            this.last.revalue(result)
            this.owner.lifecycle.addObserver(this)
        }
    }

    //TODO test
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun resumed() {
        if (this.last.hasValue()) {
            this.callback(this.last.value())
            this.owner.lifecycle.removeObserver(this)
            this.last.empty()
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun destroyed() {
        if (this.last.hasValue()) {
            this.owner.lifecycle.removeObserver(this)
            this.last.empty()
        }
    }
}