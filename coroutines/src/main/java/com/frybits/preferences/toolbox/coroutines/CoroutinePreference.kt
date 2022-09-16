package com.frybits.preferences.toolbox.coroutines

import com.frybits.preferences.toolbox.core.Preference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.withContext

interface CoroutinePreference<T>: Preference<T> {

    fun asFlow(): Flow<T>

    suspend fun commitValue(value: T): Boolean
}

fun <T> Preference<T>.asCoroutinePreference(keysChanged: Flow<String>): CoroutinePreference<T> = CoroutinePreferenceImpl(this, keysChanged)

private class CoroutinePreferenceImpl<T>(
    private val preference: Preference<T>,
    private val keysChanged: Flow<String?>
): CoroutinePreference<T>, Preference<T> by preference {

    override fun asFlow(): Flow<T> {
        return keysChanged.filter { it == key || it == null }
            .map {
                if (it != null && isSet) {
                    return@map value
                } else {
                    return@map defaultValue
                }
            }.onStart { emit(value) }
    }

    override suspend fun commitValue(value: T): Boolean {
        return withContext(Dispatchers.IO) {
            val editor = sharedPreferences.edit()
            adapter.set(key, value, editor)
            return@withContext editor.commit()
        }
    }
}
