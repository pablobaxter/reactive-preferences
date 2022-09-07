package com.frybits.preferences.toolbox.core

import android.content.SharedPreferences
import androidx.core.content.edit

/**
 * Created by Pablo Baxter (Github: pablobaxter)
 */
interface Preference<T> {

    interface Converter<T> {

        fun <T> deserialize(serialized: String): T

        fun serialize(value: T): String
    }

    interface Adapter<T> {
        fun get(key: String, sharedPreference: SharedPreferences, defaultValue: T): T

        fun set(key: String, value: T, editor: SharedPreferences.Editor)
    }

    val key: String

    val defaultValue: T

    var value: T

    val isSet: Boolean

    fun delete()
}

@Suppress("FunctionName")
internal fun <T> Preference(
    preferences: SharedPreferences,
    key: String,
    defaultValue: T,
    adapter: Preference.Adapter<T>
): Preference<T> = PreferenceImpl(
    sharedPreference = preferences,
    _key = key,
    _defaultValue = defaultValue,
    adapter = adapter
)

private class PreferenceImpl<T>(
    private val sharedPreference: SharedPreferences,
    private val _key: String,
    private val _defaultValue: T,
    private val adapter: Preference.Adapter<T>
): Preference<T> {

    override val key: String
        get() = _key

    override val defaultValue: T
        get() = _defaultValue

    override var value: T
        get() = adapter.get(_key, sharedPreference, _defaultValue)
        set(value) {
            sharedPreference.edit {
                adapter.set(_key, value, this)
            }
        }

    override val isSet: Boolean
        get() = sharedPreference.contains(_key)

    override fun delete() {
        sharedPreference.edit { remove(_key) }
    }
}
