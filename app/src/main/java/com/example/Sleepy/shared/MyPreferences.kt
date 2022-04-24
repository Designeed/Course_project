package com.example.Sleepy.shared

import android.content.Context
import android.content.SharedPreferences
import com.example.Sleepy.R

class MyPreferences {
    class SettingsApp(context: Context) {
        private val preferences: SharedPreferences =
            context.getSharedPreferences("SETTINGS", Context.MODE_PRIVATE)

        fun clearSettingsApp() {
            preferences.edit().clear().apply()
        }

        var themeId: Int
            get() = preferences.getInt("THEME", R.id.rbThemeAuto)
            set(themeId) {
                preferences.edit().putInt("THEME", themeId).apply()
            }

        var isAnimated: Boolean
            get() = preferences.getBoolean("ANIMATIONS", true)
            set(isAnimated) {
                preferences.edit().putBoolean("ANIMATIONS", isAnimated).apply()
            }

        var cardCount: Int
            get() = preferences.getInt("CARD_COUNT", 6)
            set(cardCount) {
                preferences.edit().putInt("CARD_COUNT", cardCount).apply()
            }

        var isCheckedSleepTime: Boolean
            get() = preferences.getBoolean("CHECK_SLEEP", false)
            set(check) {
                preferences.edit().putBoolean("CHECK_SLEEP", check).apply()
            }

        var sleepTime: Int
            get() = preferences.getInt("SLEEP_TIME", 0)
            set(time) {
                preferences.edit().putInt("SLEEP_TIME", time).apply()
            }

        fun is24TimeFormat(): Boolean {
            return preferences.getBoolean("TIME_FORMAT", true)
        }

        fun set24TimeFormat(format: Boolean) {
            preferences.edit().putBoolean("TIME_FORMAT", format).apply()
        }

        var cycleDuration: Int
            get() = preferences.getInt("CYCLE_DURATION", 90)
            set(duration) {
                preferences.edit().putInt("CYCLE_DURATION", duration).apply()
            }

        var isCheckedQuotes: Boolean
            get() = preferences.getBoolean("QUOTE", true)
            set(check) {
                preferences.edit().putBoolean("QUOTE", check).apply()
            }

        val isBuiltinAlarm: Boolean
            get() = preferences.getBoolean("WHAT_ALARM", true)

        fun setCheckedBuiltinAlarm(check: Boolean) {
            preferences.edit().putBoolean("WHAT_ALARM", check).apply()
        }

        var isVibrated: Boolean
            get() = preferences.getBoolean("VIBRATIONS", true)
            set(check) {
                preferences.edit().putBoolean("VIBRATIONS", check).apply()
            }

        var extraTime: Int
            get() = preferences.getInt("EXTRA_TIME", 10)
            set(minutes) {
                preferences.edit().putInt("EXTRA_TIME", minutes).apply()
            }

    }

    class RunApp(context: Context) {
        private val preferences: SharedPreferences =
            context.getSharedPreferences("SETTINGS_RUN", Context.MODE_PRIVATE)

        fun clearRunApp() {
            preferences.edit().clear().apply()
        }

        val isFirstRun: Boolean
            get() = preferences.getBoolean("FIRST_RUN", true)

        fun setCheckedRun(checkedRun: Boolean) {
            preferences.edit().putBoolean("FIRST_RUN", checkedRun).apply()
        }

    }
}