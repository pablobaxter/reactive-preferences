package com.frybits.preferences.core

import android.content.SharedPreferences
import androidx.core.content.edit

abstract class CoreSharedPreferences(protected val sharedPreferences: SharedPreferences) {

    protected abstract fun getBoolean(key: String?, defaultValue: Boolean): Preference<Boolean>

    protected abstract fun <T: Enum<T>> getEnum(key: String?, defaultValue: T, clazz: Class<T>): Preference<T>

    protected abstract fun getFloat(key: String?, defaultValue: Float): Preference<Float>

    protected abstract fun getInteger(key: String?, defaultValue: Int): Preference<Int>

    protected abstract fun getLong(key: String?, defaultValue: Long): Preference<Long>

    protected abstract fun <T> getObject(key: String?, defaultValue: T, converter: Preference.Converter<T>): Preference<T>

    protected abstract fun getString(key: String?, defaultValue: String?): Preference<String?>

    protected abstract fun getStringSet(key: String?, defaultValue: Set<String?>?): Preference<Set<String?>?>

    fun clear() {
        sharedPreferences.edit { clear() }
    }
}
