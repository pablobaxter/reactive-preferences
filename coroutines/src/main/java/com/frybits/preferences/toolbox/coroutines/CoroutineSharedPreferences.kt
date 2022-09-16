package com.frybits.preferences.toolbox.coroutines

import android.content.SharedPreferences
import com.frybits.preferences.toolbox.core.BooleanAdapter
import com.frybits.preferences.toolbox.core.CoreSharedPreferences
import com.frybits.preferences.toolbox.core.EnumAdapter
import com.frybits.preferences.toolbox.core.Preference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.shareIn
import kotlin.reflect.KClass

class CoroutineSharedPreferences internal constructor(
    sharedPreferences: SharedPreferences,
    scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
): CoreSharedPreferences(sharedPreferences) {

    companion object {
        fun using(sharedPreferences: SharedPreferences): CoroutineSharedPreferences {
            return CoroutineSharedPreferences(sharedPreferences)
        }
    }

    private val keyChanges = callbackFlow<String> {
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { prefs, key ->
            check(prefs === sharedPreferences) { "CoroutinePreferences not listening to the right SharedPreferences" }
            trySendBlocking(key)
        }

        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)

        awaitClose { sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener) }
    }.shareIn(scope, SharingStarted.WhileSubscribed())

    public override fun getBoolean(key: String?, defaultValue: Boolean): CoroutinePreference<Boolean> {
        return Preference(sharedPreferences, key, defaultValue, BooleanAdapter).asCoroutinePreference(keyChanges)
    }

    override fun <T : Enum<T>> getEnum(key: String?, defaultValue: T, clazz: KClass<T>): Preference<T> {
        return Preference(sharedPreferences, key, defaultValue, EnumAdapter(clazz)).asCoroutinePreference(keyChanges)
    }
}
