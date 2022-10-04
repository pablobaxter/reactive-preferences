package com.frybits.preferences.app

import android.app.Activity
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.CheckBox
import android.widget.CompoundButton
import com.frybits.preferences.coroutine.CoroutinePreference
import com.frybits.preferences.coroutine.CoroutineSharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import reactivecircus.flowbinding.android.widget.checkedChanges

class MainActivity: Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val blah = CoroutineSharedPreferences.create(PreferenceManager.getDefaultSharedPreferences(this)).getBoolean("blah")

        GlobalScope.launch {
            CheckBox(this@MainActivity).checkedChanges().skipInitialValue().collect(blah.asCollector())
        }
    }
}