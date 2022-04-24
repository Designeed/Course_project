package com.example.Sleepy.main.modules.settings.domain

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SettingsViewModel : ViewModel() {
    var autoTheme: MutableLiveData<Boolean>? = null
    var showAnimations: MutableLiveData<Boolean>? = null
    var checkSleep: MutableLiveData<Boolean>? = null
    var timeFormat: MutableLiveData<Boolean>? = null
    var Apptheme: MutableLiveData<Int>? = null
    var cardCount: MutableLiveData<Int>? = null
    var timeSleep: MutableLiveData<Int>? = null
    var sleepCycle: MutableLiveData<Int>? = null
    private val prefs: SharedPreferences? = null
}