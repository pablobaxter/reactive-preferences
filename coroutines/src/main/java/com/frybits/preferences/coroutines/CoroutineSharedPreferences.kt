package com.frybits.preferences.coroutines

import android.content.SharedPreferences
import androidx.annotation.VisibleForTesting
import com.frybits.preferences.core.BooleanAdapter
import com.frybits.preferences.core.ConverterAdapter
import com.frybits.preferences.core.CoreSharedPreferences
import com.frybits.preferences.core.EnumAdapter
import com.frybits.preferences.core.FloatAdapter
import com.frybits.preferences.core.IntegerAdapter
import com.frybits.preferences.core.LongAdapter
import com.frybits.preferences.core.Preference
import com.frybits.preferences.core.StringAdapter
import com.frybits.preferences.core.StringSetAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.withContext

class CoroutineSharedPreferences @VisibleForTesting internal constructor(
    sharedPreferences: SharedPreferences,
    overrideKeyChanges: Flow<String?>?,
    scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
): CoreSharedPreferences(sharedPreferences) {

    companion object {

        @JvmStatic
        @JvmOverloads
        fun create(sharedPreferences: SharedPreferences, keyChanges: Flow<String?>? = null): CoroutineSharedPreferences {
            return CoroutineSharedPreferences(sharedPreferences, keyChanges)
        }
    }

    private val keyChanges: Flow<String?> = overrideKeyChanges ?: callbackFlow<String?> {
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { prefs, key ->
            check(prefs === sharedPreferences) { "CoroutinePreferences not listening to the right SharedPreferences" }
            trySendBlocking(key)
        }

        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)

        awaitClose { sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener) }
    }.shareIn(scope, SharingStarted.WhileSubscribed())

    fun getBoolean(key: String?): CoroutinePreference<Boolean> {
        return getBoolean(key, false)
    }

    public override fun getBoolean(key: String?, defaultValue: Boolean): CoroutinePreference<Boolean> {
        return Preference(sharedPreferences, key, defaultValue, BooleanAdapter).asCoroutinePreference(keyChanges)
    }

    inline fun <reified T: Enum<T>> getEnum(key: String?, defaultValue: T): CoroutinePreference<T> {
        return getEnum(key, defaultValue, T::class.java)
    }

    public override fun <T : Enum<T>> getEnum(key: String?, defaultValue: T, clazz: Class<T>): CoroutinePreference<T> {
        return Preference(sharedPreferences, key, defaultValue, EnumAdapter(clazz)).asCoroutinePreference(keyChanges)
    }

    fun getFloat(key: String?): CoroutinePreference<Float> {
        return getFloat(key, 0F)
    }

    public override fun getFloat(key: String?, defaultValue: Float): CoroutinePreference<Float> {
        return Preference(sharedPreferences, key, defaultValue, FloatAdapter).asCoroutinePreference(keyChanges)
    }

    fun getInteger(key: String?): CoroutinePreference<Int> {
        return getInteger(key, 0)
    }

    public override fun getInteger(key: String?, defaultValue: Int): CoroutinePreference<Int> {
        return Preference(sharedPreferences, key, defaultValue, IntegerAdapter).asCoroutinePreference(keyChanges)
    }

    fun getLong(key: String?): CoroutinePreference<Long> {
        return getLong(key, 0L)
    }

    public override fun getLong(key: String?, defaultValue: Long): CoroutinePreference<Long> {
        return Preference(sharedPreferences, key, defaultValue, LongAdapter).asCoroutinePreference(keyChanges)
    }

    public override fun <T> getObject(key: String?, defaultValue: T, converter: Preference.Converter<T>): CoroutinePreference<T> {
        return Preference(sharedPreferences, key, defaultValue, ConverterAdapter(converter)).asCoroutinePreference(keyChanges)
    }

    fun getString(key: String?): CoroutinePreference<String?> {
        return getString(key, null)
    }

    public override fun getString(key: String?, defaultValue: String?): CoroutinePreference<String?> {
        return Preference(sharedPreferences, key, defaultValue, StringAdapter).asCoroutinePreference(keyChanges)
    }

    fun getStringSet(key: String?): CoroutinePreference<Set<String?>?> {
        return getStringSet(key, null)
    }

    public override fun getStringSet(key: String?, defaultValue: Set<String?>?): CoroutinePreference<Set<String?>?> {
        return Preference(sharedPreferences, key, defaultValue, StringSetAdapter).asCoroutinePreference(keyChanges)
    }

    suspend fun clearSync(): Boolean {
        return withContext(Dispatchers.IO) {
            return@withContext sharedPreferences.edit().clear().commit()
        }
    }
}
