package com.frybits.preferences.toolbox.core

import android.content.SharedPreferences
import kotlin.reflect.KClass

abstract class CoreSharedPreferences(
    protected val sharedPreferences: SharedPreferences
) {

    protected abstract fun getBoolean(key: String?, defaultValue: Boolean = false): Preference<Boolean>

    protected abstract fun <T: Enum<T>> getEnum(key: String?, defaultValue: T, clazz: Class<T>): Preference<T>

    protected abstract fun getFloat(key: String?, defaultValue: Float = 0F): Preference<Float>

    protected abstract fun getInteger(key: String?, defaultValue: Int = 0): Preference<Int>

    protected abstract fun getLong(key: String?, defaultValue: Long = 0L): Preference<Long>

    protected abstract fun <T> getObject(key: String?, defaultValue: T? = null, converter: Preference.Converter<T>): Preference<T?>

    protected abstract fun getString(key: String?, defaultValue: String? = null): Preference<String?>

    protected abstract fun getStringSet(key: String?, defaultValue: Set<String?>? = null): Preference<Set<String?>?>
}
