package com.frybits.preferences.livedata

import android.content.SharedPreferences
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import org.junit.Rule
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

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

class LiveDataSharedPreferencesTest {

    @get:Rule
    val executorRule = InstantTaskExecutorRule()

    private lateinit var keysChangedLiveData: MutableLiveData<String?>
    private lateinit var liveDataSharedPreferences: LiveDataSharedPreferences

    @Mock
    private lateinit var sharedPreferences: SharedPreferences

    @BeforeTest
    fun setup() {
        MockitoAnnotations.openMocks(this)

        keysChangedLiveData = MutableLiveData()
        liveDataSharedPreferences = LiveDataSharedPreferences(sharedPreferences, keysChangedLiveData)
    }

    @AfterTest
    fun tearDown() {
        Mockito.clearAllCaches()
    }

    @Test
    fun testKey() {
        val liveDataPreference = liveDataSharedPreferences.getBoolean("test")
        assertEquals("test", liveDataPreference.key)
    }

    @Test
    fun testCoroutinePreferencesDefaults() {
        assertEquals(false, liveDataSharedPreferences.getBoolean("test").defaultValue)
        assertEquals(0F, liveDataSharedPreferences.getFloat("test").defaultValue)
        assertEquals(0, liveDataSharedPreferences.getInteger("test").defaultValue)
        assertEquals(0L, liveDataSharedPreferences.getLong("test").defaultValue)
        assertNull(liveDataSharedPreferences.getString("test").defaultValue)
        assertNull(liveDataSharedPreferences.getStringSet("test").defaultValue)
    }
}
