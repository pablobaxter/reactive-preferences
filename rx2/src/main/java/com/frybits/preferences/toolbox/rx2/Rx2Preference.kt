package com.frybits.preferences.toolbox.rx2

import com.frybits.preferences.toolbox.core.CorePreference
import io.reactivex.Observable
import io.reactivex.functions.Consumer

interface Rx2Preference<T>: CorePreference<T> {

    fun asObservable(): Observable<T>

    fun asConsumer(): Consumer<in T>
}

internal fun <T> CorePreference<T>.asRx2Preference(keysChanged: Observable<Optional<String?>>): Rx2Preference<T> = Rx2PreferenceImpl(this, keysChanged)

private class Rx2PreferenceImpl<T>(
    private val preference: CorePreference<T>,
    private val keysChanged: Observable<Optional<String?>>
): Rx2Preference<T>, CorePreference<T> by preference {

    @Deprecated("Use `this.value = value` instead.", level = DeprecationLevel.ERROR)
    override fun set(value: T) {
        requireNotNull(value) { "Rx2SharedPreferences does not allow null values." }
        this.value = value
    }

    override var value: T
        get() = preference.value
        set(value) {
            requireNotNull(value) { "Rx2SharedPreferences does not allow null values." }
            preference.value = value
        }

    override fun asObservable(): Observable<T> {
        return keysChanged.filter { it.value == key }
            .startWith(Optional("")).map { value }
    }

    override fun asConsumer(): Consumer<in T> {
        return Consumer {
            value = it
        }
    }
}
