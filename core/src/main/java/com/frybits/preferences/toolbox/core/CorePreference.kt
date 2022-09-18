package com.frybits.preferences.toolbox.core

import android.content.SharedPreferences
import androidx.core.content.edit

/**
 * Created by Pablo Baxter (Github: pablobaxter)
 */
interface CorePreference<T> {

    interface Converter<T> {

        fun deserialize(serialized: String): T

        fun serialize(value: T): String
    }

    val sharedPreferences: SharedPreferences

    val adapter: Adapter<T>

    val key: String?

    val defaultValue: T

    var value: T

    val isSet: Boolean

    fun delete()
}

@Suppress("FunctionName")
fun <T> CorePreference(
    preferences: SharedPreferences,
    key: String?,
    defaultValue: T,
    adapter: Adapter<T>
): CorePreference<T> = PreferenceImpl(
    sharedPreferences = preferences,
    _key = key,
    _defaultValue = defaultValue,
    adapter = adapter
)

private class PreferenceImpl<T>(
    override val sharedPreferences: SharedPreferences,
    private val _key: String?,
    private val _defaultValue: T,
    override val adapter: Adapter<T>
): CorePreference<T> {

    override val key: String?
        get() = _key

    override val defaultValue: T
        get() = _defaultValue

    override var value: T
        get() = adapter.get(_key, sharedPreferences, _defaultValue)
        set(value) {
            sharedPreferences.edit {
                adapter.set(_key, value, this)
            }
        }

    override val isSet: Boolean
        get() = sharedPreferences.contains(_key)

    override fun delete() {
        sharedPreferences.edit { remove(_key) }
    }
}
