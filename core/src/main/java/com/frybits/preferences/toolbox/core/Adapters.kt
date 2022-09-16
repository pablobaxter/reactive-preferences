package com.frybits.preferences.toolbox.core

import android.content.SharedPreferences
import kotlin.reflect.KClass

interface Adapter<T> {
    fun get(key: String?, sharedPreference: SharedPreferences, defaultValue: T): T

    fun set(key: String?, value: T, editor: SharedPreferences.Editor)
}

object BooleanAdapter: Adapter<Boolean> {
    override fun get(key: String?, sharedPreference: SharedPreferences, defaultValue: Boolean): Boolean {
        return sharedPreference.getBoolean(key, defaultValue)
    }

    override fun set(key: String?, value: Boolean, editor: SharedPreferences.Editor) {
        editor.putBoolean(key, value)
    }
}

class ConverterAdapter<T>(private val converter: Preference.Converter<T>): Adapter<T> {
    override fun get(key: String?, sharedPreference: SharedPreferences, defaultValue: T): T {
        val serialized = sharedPreference.getString(key, null) ?: return defaultValue
        return converter.deserialize(serialized)
    }

    override fun set(key: String?, value: T, editor: SharedPreferences.Editor) {
        val serialized = converter.serialize(value)
        editor.putString(key, serialized)
    }
}

class EnumAdapter<T : Enum<T>>(private val clazz: KClass<T>): Adapter<T> {

    override fun get(key: String?, sharedPreference: SharedPreferences, defaultValue: T): T {
        val value = sharedPreference.getString(key, null) ?: return defaultValue
        return try {
            java.lang.Enum.valueOf(clazz.java, value)
        } catch (e: IllegalArgumentException) {
            defaultValue
        }
    }

    override fun set(key: String?, value: T, editor: SharedPreferences.Editor) {
        editor.putString(key, value.name)
    }
}

object FloatAdapter: Adapter<Float> {
    override fun get(key: String?, sharedPreference: SharedPreferences, defaultValue: Float): Float {
        return sharedPreference.getFloat(key, defaultValue)
    }

    override fun set(key: String?, value: Float, editor: SharedPreferences.Editor) {
        editor.putFloat(key, value)
    }
}

object IntegerAdapter: Adapter<Int> {
    override fun get(key: String?, sharedPreference: SharedPreferences, defaultValue: Int): Int {
        return sharedPreference.getInt(key, defaultValue)
    }

    override fun set(key: String?, value: Int, editor: SharedPreferences.Editor) {
        editor.putInt(key, value)
    }
}

object LongAdapter: Adapter<Long> {
    override fun get(key: String?, sharedPreference: SharedPreferences, defaultValue: Long): Long {
        return sharedPreference.getLong(key, defaultValue)
    }

    override fun set(key: String?, value: Long, editor: SharedPreferences.Editor) {
        editor.putLong(key, value)
    }
}

object StringAdapter: Adapter<String?> {
    override fun get(key: String?, sharedPreference: SharedPreferences, defaultValue: String?): String? {
        return sharedPreference.getString(key, defaultValue)
    }

    override fun set(key: String?, value: String?, editor: SharedPreferences.Editor) {
        editor.putString(key, value)
    }
}

object StringSetAdapter: Adapter<Set<String>?> {
    override fun get(key: String?, sharedPreference: SharedPreferences, defaultValue: Set<String>?): Set<String>? {
        return sharedPreference.getStringSet(key, defaultValue)
    }

    override fun set(key: String?, value: Set<String>?, editor: SharedPreferences.Editor) {
        editor.putStringSet(key, value)
    }
}
