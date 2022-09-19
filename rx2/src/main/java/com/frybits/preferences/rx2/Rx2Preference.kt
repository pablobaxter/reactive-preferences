package com.frybits.preferences.rx2

import com.frybits.preferences.core.Preference
import io.reactivex.Observable
import io.reactivex.functions.Consumer

interface Rx2Preference<T>: Preference<T> {

    @Deprecated(message = "Use `key` instead.", replaceWith = ReplaceWith("key"), level = DeprecationLevel.ERROR)
    fun key(): String? {
        return key
    }

    @Deprecated(message = "Use `defaultValue` instead.", replaceWith = ReplaceWith("defaultValue"), level = DeprecationLevel.ERROR)
    fun defaultValue(): T {
        return defaultValue
    }

    @Deprecated(message = "Use `value` instead.", replaceWith = ReplaceWith("value"), level = DeprecationLevel.ERROR)
    fun get(): T {
        return value
    }

    // Unable to use ReplaceWith, due to bug with setters. https://youtrack.jetbrains.com/issue/KTIJ-12836/ReplaceWith-cannot-replace-function-invocation-with-property-assignment
    @Deprecated(message = "Use `this.value = value` instead.", level = DeprecationLevel.ERROR)
    fun set(value: T) {
        this.value = value
    }

    fun asObservable(): Observable<T>

    fun asConsumer(): Consumer<in T>
}

internal fun <T> Preference<T>.asRx2Preference(keysChanged: Observable<Optional<String?>>): Rx2Preference<T> = Rx2PreferenceImpl(this, keysChanged)

private class Rx2PreferenceImpl<T>(
    private val preference: Preference<T>,
    private val keysChanged: Observable<Optional<String?>>
): Rx2Preference<T>, Preference<T> by preference {

    @Deprecated("Use `this.value = value` instead.", level = DeprecationLevel.ERROR)
    override fun set(value: T) {
        requireNotNull(value) { throw NullPointerException("value == null") }
        this.value = value
    }

    override var value: T
        get() = preference.value
        set(value) {
            requireNotNull(value) { throw NullPointerException("value == null") }
            preference.value = value
        }

    override fun asObservable(): Observable<T> {
        return keysChanged.filter { it.value == key || it.value == null }
            .startWith(Optional(""))
            .map { value }
    }

    override fun asConsumer(): Consumer<in T> {
        return Consumer {
            value = it
        }
    }
}
