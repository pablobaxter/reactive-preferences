package com.frybits.preferences.core

import android.content.SharedPreferences
import androidx.annotation.RestrictTo

/*
 *  Copyright 2022 Pablo Baxter
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 * Created by Pablo Baxter (Github: pablobaxter)
 * https://github.com/pablobaxter/reactive-preferences
 *
 */

/** A preference of type [T]. Instances are created from [FrybitsSharedPreferences] factories. */
interface Preference<T> {

    /** Converts instances of [T] to be stored and retrieved as Strings in [SharedPreferences]. */
    interface Converter<T> {

        /** Deserialize to an instance of [T]. The input is retrieved from [SharedPreferences.getString]. */
        fun deserialize(serialized: String?): T

        /**
         * Serialize the [value] to a [String]. The result will be used with
         * [SharedPreferences.Editor.putString].
         */
        fun serialize(value: T): String?
    }

    /** The [SharedPreferences] that backs this preference */
    val sharedPreferences: SharedPreferences

    /** The adapter to use for the given type [T] */
    val adapter: Adapter<T>

    /** The key used to store the preference */
    val key: String?

    /** The default value to return when this preference is not set */
    val defaultValue: T

    /** The current value of the preference */
    var value: T

    /** Flag to indicate if the preference has been set */
    val isSet: Boolean

    /** Delete the preference from the underlying [SharedPreferences] */
    fun delete()
}

/**
 * Creates a [Preference]. This object does not provide any reactive streams, and is considered a base object for the reactive libraries.
 *
 * @suppress **This function should be internal, but used by library group**
 */
@Suppress("FunctionName")
@JvmSynthetic
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
fun <T> Preference(
    preferences: SharedPreferences,
    key: String?,
    defaultValue: T,
    adapter: Adapter<T>
): Preference<T> = PreferenceImpl(
    sharedPreferences = preferences,
    key = key,
    defaultValue = defaultValue,
    adapter = adapter
)

private class PreferenceImpl<T>(
    override val sharedPreferences: SharedPreferences,
    override val key: String?,
    override val defaultValue: T,
    override val adapter: Adapter<T>
): Preference<T> {

    override var value: T
        get() = adapter.get(key, sharedPreferences, defaultValue)
        set(value) {
            with(sharedPreferences.edit()) {
                adapter.set(key, value, this)
                apply()
            }
        }

    override val isSet: Boolean
        get() = sharedPreferences.contains(key)

    override fun delete() {
        sharedPreferences.edit().remove(key).apply()
    }
}
