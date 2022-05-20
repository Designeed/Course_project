package com.example.sleepy.data.storage

import android.content.Context
import android.content.SharedPreferences
import com.example.sleepy.R
import com.example.sleepy.utils.Constants

class PrefsStorage(context: Context) {
    private val preferences: SharedPreferences =
        context.getSharedPreferences(Constants.MAIN_PREFS_NAME, Context.MODE_PRIVATE)

    fun clearSettingsApp() {
        preferences.edit().clear().apply()
    }

    var themeId: Int
        get() = preferences.getInt(Constants.THEME_KEY, R.id.rbThemeAuto)
        set(themeId) {
            preferences.edit().putInt(Constants.THEME_KEY, themeId).apply()
        }

    var isAnimated: Boolean
        get() = preferences.getBoolean(Constants.ANIMATIONS_KEY, true)
        set(isAnimated) {
            preferences.edit().putBoolean(Constants.ANIMATIONS_KEY, isAnimated).apply()
        }

    var cardCount: Int
        get() = preferences.getInt(Constants.CARD_COUNT_KEY, 6)
        set(cardCount) {
            preferences.edit().putInt(Constants.CARD_COUNT_KEY, cardCount).apply()
        }

    var isCheckedSleepTime: Boolean
        get() = preferences.getBoolean(Constants.CHECK_SLEEP_KEY, false)
        set(check) {
            preferences.edit().putBoolean(Constants.CHECK_SLEEP_KEY, check).apply()
        }

    var sleepTime: Int
        get() = preferences.getInt(Constants.SLEEP_TIME_KEY, 0)
        set(time) {
            preferences.edit().putInt(Constants.SLEEP_TIME_KEY, time).apply()
        }

    var is24TimeFormat: Boolean
        get() = preferences.getBoolean(Constants.TIME_FORMAT_KEY, true)
        set(check){
            preferences.edit().putBoolean(Constants.TIME_FORMAT_KEY, check).apply()
        }

    var cycleDuration: Int
        get() = preferences.getInt(Constants.CYCLE_DURATION_KEY, 90)
        set(duration) {
            preferences.edit().putInt(Constants.CYCLE_DURATION_KEY, duration).apply()
        }

    var isCheckedQuotes: Boolean
        get() = preferences.getBoolean(Constants.QUOTE_KEY, true)
        set(check) {
            preferences.edit().putBoolean(Constants.QUOTE_KEY, check).apply()
        }

    var isBuiltinAlarm: Boolean
        get() = preferences.getBoolean(Constants.WHAT_ALARM_KEY, true)
        set(check){
            preferences.edit().putBoolean(Constants.WHAT_ALARM_KEY, check).apply()
        }

    var isVibrated: Boolean
        get() = preferences.getBoolean(Constants.VIBRATIONS_KEY, true)
        set(check) {
            preferences.edit().putBoolean(Constants.VIBRATIONS_KEY, check).apply()
        }

    var extraTime: Int
        get() = preferences.getInt(Constants.EXTRA_TIME_KEY, 10)
        set(minutes) {
            preferences.edit().putInt(Constants.EXTRA_TIME_KEY, minutes).apply()
        }

}