package com.frybits.preferences.rx2

import android.content.SharedPreferences
import androidx.annotation.CheckResult
import androidx.annotation.VisibleForTesting
import com.frybits.preferences.core.BooleanAdapter
import com.frybits.preferences.core.EnumAdapter
import com.frybits.preferences.core.FloatAdapter
import com.frybits.preferences.core.FrybitsSharedPreferences
import com.frybits.preferences.core.IntegerAdapter
import com.frybits.preferences.core.LongAdapter
import com.frybits.preferences.core.Preference
import io.reactivex.Observable

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

/**
 * Preference factory class to provide [Rx2Preference] objects
 */
class Rx2SharedPreferences @VisibleForTesting internal constructor(
    sharedPreferences: SharedPreferences,
    overrideKeyChanges: Observable<Optional<String?>>?
): FrybitsSharedPreferences(sharedPreferences) {

    companion object {

        /**
         * Creates a [Rx2SharedPreferences] backed by the [SharedPreferences] passed in
         *
         * @param sharedPreferences The preferences to back this factory
         * @param keyChanges Optional [Observable] that this factory will use to listen for key changes
         *
         * @return A new instance of [Rx2SharedPreferences]
         */
        @JvmStatic
        @JvmOverloads
        fun create(sharedPreferences: SharedPreferences, keyChanges: Observable<String>? = null): Rx2SharedPreferences {
            return Rx2SharedPreferences(sharedPreferences, keyChanges?.map { it.asOptional() })
        }
    }

    // Either use the key changes flow passed in, or the default flow
    private val keyChanges: Observable<Optional<String?>> = overrideKeyChanges ?: Observable.create<Optional<String?>> { emitter ->
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { prefs, key ->
            check(prefs === sharedPreferences) { "Rx2SharedPreferences not listening to the right SharedPreferences" }
            emitter.onNext(key.asOptional()) // Handle `null` values
        }

        emitter.setCancellable {
            sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener)
        }

        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
    }.share()

    /** Creates a [Boolean] preference for the [key] with a default of `false` */
    @CheckResult
    fun getBoolean(key: String?): Rx2Preference<Boolean> {
        return getBoolean(key, false)
    }

    /** Creates a [Boolean] preference for the [key] with a default of [defaultValue] */
    @CheckResult
    public override fun getBoolean(key: String?, defaultValue: Boolean): Rx2Preference<Boolean> {
        return Preference(sharedPreferences, key, defaultValue, BooleanAdapter).asRx2Preference(keyChanges)
    }

    /** Creates a [T] preference for the [key] using the enum class type [T] with a default of [defaultValue] */
    @CheckResult
    inline fun <reified T: Enum<T>> getEnum(key: String?, defaultValue: T): Rx2Preference<T> {
        return getEnum(key, defaultValue, T::class.java)
    }

    /** Creates a [T] preference for the [key] using the enum class [clazz] with a default of [defaultValue] */
    @CheckResult
    public override fun <T : Enum<T>> getEnum(key: String?, defaultValue: T, clazz: Class<T>): Rx2Preference<T> {
        return Preference(sharedPreferences, key, defaultValue, EnumAdapter(clazz)).asRx2Preference(keyChanges)
    }

    /** Creates a [Float] preference for the [key] with a default of `0F` */
    @CheckResult
    fun getFloat(key: String?): Rx2Preference<Float> {
        return getFloat(key, 0F)
    }

    /** Creates a [Float] preference for the [key] with a default of [defaultValue] */
    @CheckResult
    public override fun getFloat(key: String?, defaultValue: Float): Rx2Preference<Float> {
        return Preference(sharedPreferences, key, defaultValue, FloatAdapter).asRx2Preference(keyChanges)
    }

    /** Creates a [Int] preference for the [key] with a default of `0` */
    @CheckResult
    fun getInteger(key: String?): Rx2Preference<Int> {
        return getInteger(key, 0)
    }

    /** Creates a [Int] preference for the [key] with a default of [defaultValue] */
    @CheckResult
    public override fun getInteger(key: String?, defaultValue: Int): Rx2Preference<Int> {
        return Preference(sharedPreferences, key, defaultValue, IntegerAdapter).asRx2Preference(keyChanges)
    }

    /** Creates a [Long] preference for the [key] with a default of `0L` */
    @CheckResult
    fun getLong(key: String?): Rx2Preference<Long> {
        return getLong(key, 0L)
    }

    /** Creates a [Long] preference for the [key] with a default of [defaultValue] */
    @CheckResult
    public override fun getLong(key: String?, defaultValue: Long): Rx2Preference<Long> {
        return Preference(sharedPreferences, key, defaultValue, LongAdapter).asRx2Preference(keyChanges)
    }

    /** Creates a [T] preference for the [key] using the [converter], and with a default of [defaultValue]. This function ensures objects in stream are not `null`. */
    @CheckResult
    public override fun <T: Any> getObjectNonNull(key: String?, defaultValue: T, converter: Preference.Converter<T>): Rx2Preference<T> {
        // Although the generic allows for nullable types, this function will throw an exception if a null value is used
        return Preference(sharedPreferences, key, defaultValue, Rx2ConverterAdapter(converter)).asRx2Preference(keyChanges)
    }

    /** Creates a [String] preference for the [key] with a default of an empty string */
    @CheckResult
    fun getString(key: String?): Rx2Preference<String> {
        return getString(key, "") // Rx2 doesn't allow null values
    }

    /** Creates a [String] preference for the [key] with a default of [defaultValue] */
    @CheckResult
    public override fun getString(key: String?, defaultValue: String?): Rx2Preference<String> {
        // Although the generic allows for nullable types, this function will throw an exception if a null value is used
        requireNotNull(defaultValue) { throw NullPointerException("defaultValue == null") }
        return Preference(sharedPreferences, key, defaultValue, NonNullStringAdapter).asRx2Preference(keyChanges)
    }

    /** Creates a string [Set] preference for the [key] with a default of [emptySet] */
    @CheckResult
    fun getStringSet(key: String?): Rx2Preference<Set<String?>> {
        return getStringSet(key, emptySet()) // Rx2 doesn't allow null values
    }

    /** Creates a string [Set] preference for the [key] with a default of [defaultValue] */
    @CheckResult
    public override fun getStringSet(key: String?, defaultValue: Set<String?>?): Rx2Preference<Set<String?>> {
        // Although the generic allows for nullable types, this function will throw an exception if a null value is used
        requireNotNull(defaultValue) { throw NullPointerException("defaultValue == null") }
        return Preference(sharedPreferences,key, defaultValue, NonNullStringSetAdapter).asRx2Preference(keyChanges)
    }
}
