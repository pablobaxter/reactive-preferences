package com.frybits.preferences.rx2

import android.content.SharedPreferences
import com.frybits.preferences.core.Adapter
import com.frybits.preferences.core.Preference

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
 * https://github.com/pablobaxter/rx-preferences
 *
 */

// Internal adapter for RxJava to ensure parity with original Legacy function to support transition from https://github.com/f2prateek/rx-preferences/
internal class Rx2ConverterAdapter<T>(private val converter: Preference.Converter<T>): Adapter<T> {
    override fun get(key: String?, sharedPreference: SharedPreferences, defaultValue: T): T {
        val serialized = sharedPreference.getString(key, null) ?: return defaultValue
        return requireNotNull(converter.deserialize(serialized)) { throw NullPointerException("Deserialized value must not be null from string: $serialized") }
    }

    override fun set(key: String?, value: T, editor: SharedPreferences.Editor) {
        val serialized = requireNotNull(converter.serialize(value)) { throw NullPointerException("Serialized string must not be null from value: $value") }
        editor.putString(key, serialized)
    }
}
