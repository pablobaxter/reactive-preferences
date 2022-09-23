package com.frybits.preferences.rx2;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

import static com.frybits.preferences.rx2.PointPreferenceConverterKt.createPointPreferenceConverter;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;

import androidx.test.core.app.ApplicationProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

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
 * Re-use of https://github.com/f2prateek/rx-preferences/blob/master/rx-preferences/src/test/java/com/f2prateek/rx/preferences2/RxSharedPreferencesTest.java
 *
 * This file was modified to handle this library, and removes unnecessary tests, as Kotlin handles nullability already.
 */

@RunWith(RobolectricTestRunner.class) //
@SuppressLint("ApplySharedPref") //
@SuppressWarnings({ "ResourceType" }) //
public class RxSharedPreferencesTest {
    private Rx2SharedPreferences rxPreferences;

    @Before
    public void setUp() {
        SharedPreferences preferences = getDefaultSharedPreferences(ApplicationProvider.getApplicationContext());
        preferences.edit().clear().commit();
        rxPreferences = Rx2SharedPreferences.create(preferences);
    }

    @Test
    public void clearRemovesAllPreferences() {
        Rx2Preference<String> preference = rxPreferences.getString("key", "default");
        preference.set("foo");
        rxPreferences.clear();
        assertThat(preference.get()).isEqualTo("default");
    }

    @Test public void objectNullDefaultValueThrows() {
        try {
            rxPreferences.getObject("key", null, createPointPreferenceConverter());
            fail();
        } catch (NullPointerException e) {
            assertThat(e).hasMessage("defaultValue == null");
        }
    }

    @Test public void stringNullDefaultValueThrows() {
        try {
            rxPreferences.getString("key", null);
            fail();
        } catch (NullPointerException e) {
            assertThat(e).hasMessage("defaultValue == null");
        }
    }

    @Test public void stringSetNullDefaultValueThrows() {
        try {
            rxPreferences.getStringSet("key", null);
            fail();
        } catch (NullPointerException e) {
            assertThat(e).hasMessage("defaultValue == null");
        }
    }
}
