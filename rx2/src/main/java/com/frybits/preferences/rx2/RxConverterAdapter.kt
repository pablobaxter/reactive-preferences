package com.frybits.preferences.rx2

import android.content.SharedPreferences
import com.frybits.preferences.core.Adapter
import com.frybits.preferences.core.Preference

class Rx2ConverterAdapter<T>(private val converter: Preference.Converter<T>): Adapter<T> {
    override fun get(key: String?, sharedPreference: SharedPreferences, defaultValue: T): T {
        val serialized = sharedPreference.getString(key, null) ?: return defaultValue
        return requireNotNull(converter.deserialize(serialized)) { throw NullPointerException("Deserialized value must not be null from string: $serialized") }
    }

    override fun set(key: String?, value: T, editor: SharedPreferences.Editor) {
        val serialized = requireNotNull(converter.serialize(value)) { throw NullPointerException("Serialized string must not be null from value: $value") }
        editor.putString(key, serialized)
    }
}
