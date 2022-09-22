package com.frybits.preferences.core

import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions
import kotlin.test.Test
import kotlin.test.assertEquals

class PreferenceTest {

    @Test
    fun testPreferenceGet() {
        val sharedPrefs = mock<SharedPreferences> {
            on { getInt(any(), any()) } doReturn 0
        }
        val pref = Preference(sharedPrefs, "test", -1, IntegerAdapter)

        assertEquals(0, pref.value, "SharedPreferences not reached")
        verify(sharedPrefs).getInt(eq("test"), eq(-1))
        verifyNoMoreInteractions(sharedPrefs)
    }

    @Test
    fun testPreferenceSet() {
        val editor = mock<Editor>()
        val sharedPrefs = mock<SharedPreferences> {
            on { contains(any()) } doReturn true
            on { edit() } doReturn editor
        }
        val pref = Preference(sharedPrefs, "test", -1, IntegerAdapter)

        pref.value = 2
        verify(sharedPrefs).edit()
        verify(editor).putInt(eq("test"), eq(2))
        verify(editor).apply()
        verifyNoMoreInteractions(editor)
        verifyNoMoreInteractions(sharedPrefs)
    }

    @Test
    fun testIsSet() {
        val sharedPrefs = mock<SharedPreferences> {
            on { contains(any()) } doReturn true
        }
        val pref = Preference(sharedPrefs, "test", -1, IntegerAdapter)

        assertEquals(true, pref.isSet, "SharedPreferences not reached")
        verify(sharedPrefs).contains(eq("test"))
        verifyNoMoreInteractions(sharedPrefs)
    }

    @Test
    fun testDelete() {
        val editor = mock<Editor> {
            on { remove(any()) } doReturn this.mock
        }
        val sharedPrefs = mock<SharedPreferences> {
            on { contains(any()) } doReturn true
            on { edit() } doReturn editor
        }
        val pref = Preference(sharedPrefs, "test", -1, IntegerAdapter)

        pref.delete()
        verify(sharedPrefs).edit()
        verify(editor).remove(eq("test"))
        verify(editor).apply()
        verifyNoMoreInteractions(editor)
        verifyNoMoreInteractions(sharedPrefs)
    }
}
