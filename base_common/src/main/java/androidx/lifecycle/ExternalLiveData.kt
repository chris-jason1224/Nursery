package androidx.lifecycle

import java.lang.reflect.Field
import java.lang.reflect.Method

/**
 * Author:chris - jason
 * Date:2019-12-23.
 * Package:androidx.lifecycle
 */
open class ExternalLiveData<T> : MutableLiveData<T>() {

    companion object {
        var START_VERSION = LiveData.START_VERSION
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {

        if (owner.lifecycle.currentState == Lifecycle.State.DESTROYED) {
            return
        }
        try {
            //use ExternalLifecycleBoundObserver instead of LifecycleBoundObserver
            var wrapper: LifecycleBoundObserver = ExternalLifecycleBoundObserver(owner, observer)
            var result = callMethodPutIfAbsent(observer, wrapper)

            if (result != "null" && (result is LiveData<*>.LifecycleBoundObserver)) {
                throw  IllegalArgumentException("Cannot add the same observer with different lifecycles")
            }

            if (result != "null") {
                return
            }
            owner.lifecycle.addObserver(wrapper)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    protected open fun observerActiveLevel(): Lifecycle.State {
        return Lifecycle.State.CREATED
    }

    private fun callMethodPutIfAbsent(observer: Any, wrapper: Any): Any {
        var mObservers = getFieldObservers()
        var classOfSafeIterableMap: Class<*> = mObservers::class.java
        var putIfAbsent: Method = classOfSafeIterableMap.getDeclaredMethod(
            "putIfAbsent",
            Any::class.java,
            Any::class.java
        )
        putIfAbsent.isAccessible = true
        var result = putIfAbsent.invoke(mObservers, observer, wrapper)

        return result ?: "null"
    }

    private fun getFieldObservers(): Any {
        var fieldObservers: Field = LiveData::class.java.getDeclaredField("mObservers")
        fieldObservers.setAccessible(true)
        return fieldObservers.get(this)
    }

    private inner class ExternalLifecycleBoundObserver : LifecycleBoundObserver {

        constructor(owner: LifecycleOwner, observer: Observer<in T>?) : super(owner, observer)

        override fun shouldBeActive(): Boolean {
            return mOwner.lifecycle.currentState.isAtLeast(observerActiveLevel())
        }

    }

    public override fun getVersion(): Int {
        return super.getVersion();
    }

}