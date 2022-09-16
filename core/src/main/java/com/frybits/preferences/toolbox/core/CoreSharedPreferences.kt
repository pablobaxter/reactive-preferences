package com.frybits.preferences.toolbox.core

import android.content.SharedPreferences
import kotlin.reflect.KClass

abstract class CoreSharedPreferences(
    protected val sharedPreferences: SharedPreferences
) {

    protected abstract fun getBoolean(key: String, defaultValue: Boolean = false): Preference<Boolean>

    protected abstract fun <T: Enum<T>> getEnum(key: String, defaultValue: T, clazz: KClass<T>): Preference<T>
}
