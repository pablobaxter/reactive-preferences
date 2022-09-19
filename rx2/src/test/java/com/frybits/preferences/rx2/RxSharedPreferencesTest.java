package com.frybits.preferences.rx2;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;

import androidx.test.core.app.ApplicationProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

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
            rxPreferences.getObject("key", null, new PointPreferenceConverter());
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
