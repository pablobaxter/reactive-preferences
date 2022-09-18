package com.frybits.preferences.toolbox.coroutines

import android.content.SharedPreferences
import androidx.annotation.VisibleForTesting
import com.frybits.preferences.toolbox.core.BooleanAdapter
import com.frybits.preferences.toolbox.core.ConverterAdapter
import com.frybits.preferences.toolbox.core.CorePreference
import com.frybits.preferences.toolbox.core.CoreSharedPreferences
import com.frybits.preferences.toolbox.core.EnumAdapter
import com.frybits.preferences.toolbox.core.FloatAdapter
import com.frybits.preferences.toolbox.core.IntegerAdapter
import com.frybits.preferences.toolbox.core.LongAdapter
import com.frybits.preferences.toolbox.core.StringAdapter
import com.frybits.preferences.toolbox.core.StringSetAdapter
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

@Suppress("OVERLOADS_WITHOUT_DEFAULT_ARGUMENTS")
class CoroutineSharedPreferences @VisibleForTesting internal constructor(
    sharedPreferences: SharedPreferences,
    scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
): CoreSharedPreferences(sharedPreferences) {

    companion object {

        @JvmStatic
        fun create(sharedPreferences: SharedPreferences): CoroutineSharedPreferences {
            return CoroutineSharedPreferences(sharedPreferences)
        }
    }

    private val keyChanges: Flow<String?> = callbackFlow<String?> {
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
        return CorePreference(sharedPreferences, key, defaultValue, BooleanAdapter).asCoroutinePreference(keyChanges)
    }

    inline fun <reified T: Enum<T>> getEnum(key: String?, defaultValue: T): CoroutinePreference<T> {
        return getEnum(key, defaultValue, T::class.java)
    }

    public override fun <T : Enum<T>> getEnum(key: String?, defaultValue: T, clazz: Class<T>): CoroutinePreference<T> {
        return CorePreference(sharedPreferences, key, defaultValue, EnumAdapter(clazz)).asCoroutinePreference(keyChanges)
    }

    fun getFloat(key: String?): CoroutinePreference<Float> {
        return getFloat(key, 0F)
    }

    public override fun getFloat(key: String?, defaultValue: Float): CoroutinePreference<Float> {
        return CorePreference(sharedPreferences, key, defaultValue, FloatAdapter).asCoroutinePreference(keyChanges)
    }

    fun getInteger(key: String?): CoroutinePreference<Int> {
        return getInteger(key, 0)
    }

    public override fun getInteger(key: String?, defaultValue: Int): CoroutinePreference<Int> {
        return CorePreference(sharedPreferences, key, defaultValue, IntegerAdapter).asCoroutinePreference(keyChanges)
    }

    fun getLong(key: String?): CoroutinePreference<Long> {
        return getLong(key, 0L)
    }

    public override fun getLong(key: String?, defaultValue: Long): CoroutinePreference<Long> {
        return CorePreference(sharedPreferences, key, defaultValue, LongAdapter).asCoroutinePreference(keyChanges)
    }

    public override fun <T> getObject(key: String?, defaultValue: T, converter: CorePreference.Converter<T>): CoroutinePreference<T> {
        return CorePreference(sharedPreferences, key, defaultValue, ConverterAdapter(converter)).asCoroutinePreference(keyChanges)
    }

    fun getString(key: String?): CoroutinePreference<String?> {
        return getString(key, null)
    }

    public override fun getString(key: String?, defaultValue: String?): CoroutinePreference<String?> {
        return CorePreference(sharedPreferences, key, defaultValue, StringAdapter).asCoroutinePreference(keyChanges)
    }

    fun getStringSet(key: String?): CoroutinePreference<Set<String?>?> {
        return getStringSet(key, null)
    }

    public override fun getStringSet(key: String?, defaultValue: Set<String?>?): CoroutinePreference<Set<String?>?> {
        return CorePreference(sharedPreferences, key, defaultValue, StringSetAdapter).asCoroutinePreference(keyChanges)
    }

    suspend fun clearSync(): Boolean {
        return withContext(Dispatchers.IO) {
            return@withContext sharedPreferences.edit().clear().commit()
        }
    }
}
