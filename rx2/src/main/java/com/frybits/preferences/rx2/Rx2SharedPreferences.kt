package com.frybits.preferences.rx2

import android.content.SharedPreferences
import androidx.annotation.VisibleForTesting
import com.frybits.preferences.core.BooleanAdapter
import com.frybits.preferences.core.CoreSharedPreferences
import com.frybits.preferences.core.EnumAdapter
import com.frybits.preferences.core.FloatAdapter
import com.frybits.preferences.core.IntegerAdapter
import com.frybits.preferences.core.LongAdapter
import com.frybits.preferences.core.Preference
import com.frybits.preferences.core.StringAdapter
import com.frybits.preferences.core.StringSetAdapter
import io.reactivex.Observable

class Rx2SharedPreferences @VisibleForTesting internal constructor(
    sharedPreferences: SharedPreferences,
    overrideKeyChanges: Observable<Optional<String?>>?
): CoreSharedPreferences(sharedPreferences) {

    companion object {

        @JvmStatic
        @JvmOverloads
        fun create(sharedPreferences: SharedPreferences, keyChanges: Observable<String>? = null): Rx2SharedPreferences {
            return Rx2SharedPreferences(sharedPreferences, keyChanges?.map { it.asOptional() })
        }
    }

    private val keyChanges: Observable<Optional<String?>> = overrideKeyChanges ?: Observable.create<Optional<String?>> { emitter ->
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { prefs, key ->
            check(prefs === sharedPreferences) { "Rx2SharedPreferences not listening to the right SharedPreferences" }
            emitter.onNext(key.asOptional())
        }

        emitter.setCancellable {
            sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener)
        }

        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
    }.share()

    fun getBoolean(key: String?): Rx2Preference<Boolean> {
        return getBoolean(key, false)
    }

    public override fun getBoolean(key: String?, defaultValue: Boolean): Rx2Preference<Boolean> {
        return Preference(sharedPreferences, key, defaultValue, BooleanAdapter).asRx2Preference(keyChanges)
    }

    inline fun <reified T: Enum<T>> getEnum(key: String?, defaultValue: T): Rx2Preference<T> {
        return getEnum(key, defaultValue, T::class.java)
    }

    public override fun <T : Enum<T>> getEnum(key: String?, defaultValue: T, clazz: Class<T>): Rx2Preference<T> {
        return Preference(sharedPreferences, key, defaultValue, EnumAdapter(clazz)).asRx2Preference(keyChanges)
    }

    fun getFloat(key: String?): Rx2Preference<Float> {
        return getFloat(key, 0F)
    }

    public override fun getFloat(key: String?, defaultValue: Float): Rx2Preference<Float> {
        return Preference(sharedPreferences, key, defaultValue, FloatAdapter).asRx2Preference(keyChanges)
    }

    fun getInteger(key: String?): Rx2Preference<Int> {
        return getInteger(key, 0)
    }

    public override fun getInteger(key: String?, defaultValue: Int): Rx2Preference<Int> {
        return Preference(sharedPreferences, key, defaultValue, IntegerAdapter).asRx2Preference(keyChanges)
    }

    fun getLong(key: String?): Rx2Preference<Long> {
        return getLong(key, 0L)
    }

    public override fun getLong(key: String?, defaultValue: Long): Rx2Preference<Long> {
        return Preference(sharedPreferences, key, defaultValue, LongAdapter).asRx2Preference(keyChanges)
    }

    public override fun <T> getObject(key: String?, defaultValue: T, converter: Preference.Converter<T>): Rx2Preference<T> {
        requireNotNull(defaultValue) { throw NullPointerException("defaultValue == null") }
        return Preference(sharedPreferences, key, defaultValue, Rx2ConverterAdapter(converter)).asRx2Preference(keyChanges)
    }

    fun getString(key: String?): Rx2Preference<String?> {
        return getString(key, "") // Rx2 doesn't allow null values
    }

    public override fun getString(key: String?, defaultValue: String?): Rx2Preference<String?> {
        requireNotNull(defaultValue) { throw NullPointerException("defaultValue == null") }
        return Preference(sharedPreferences, key, defaultValue, StringAdapter).asRx2Preference(keyChanges)
    }

    fun getStringSet(key: String?): Rx2Preference<Set<String?>?> {
        return getStringSet(key, emptySet()) // Rx2 doesn't allow null values
    }

    public override fun getStringSet(key: String?, defaultValue: Set<String?>?): Rx2Preference<Set<String?>?> {
        requireNotNull(defaultValue) { throw NullPointerException("defaultValue == null") }
        return Preference(sharedPreferences,key, defaultValue, StringSetAdapter).asRx2Preference(keyChanges)
    }
}