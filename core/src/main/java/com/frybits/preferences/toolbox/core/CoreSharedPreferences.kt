package com.frybits.preferences.toolbox.core

import android.content.SharedPreferences
import androidx.core.content.edit

abstract class CoreSharedPreferences(protected val sharedPreferences: SharedPreferences) {

    protected abstract fun getBoolean(key: String?, defaultValue: Boolean): CorePreference<Boolean>

    protected abstract fun <T: Enum<T>> getEnum(key: String?, defaultValue: T, clazz: Class<T>): CorePreference<T>

    protected abstract fun getFloat(key: String?, defaultValue: Float): CorePreference<Float>

    protected abstract fun getInteger(key: String?, defaultValue: Int): CorePreference<Int>

    protected abstract fun getLong(key: String?, defaultValue: Long): CorePreference<Long>

    protected abstract fun <T> getObject(key: String?, defaultValue: T, converter: CorePreference.Converter<T>): CorePreference<T>

    protected abstract fun getString(key: String?, defaultValue: String?): CorePreference<String?>

    protected abstract fun getStringSet(key: String?, defaultValue: Set<String?>?): CorePreference<Set<String?>?>

    protected open fun clear() {
        sharedPreferences.edit { clear() }
    }
}
