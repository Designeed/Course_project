package com.example.sleepy.data.storage

import android.content.Context
import android.content.SharedPreferences

class RunStorage(context: Context) {
    private val preferences: SharedPreferences =
        context.getSharedPreferences("SETTINGS_RUN", Context.MODE_PRIVATE)

    var isFirstRun: Boolean
        get() = preferences.getBoolean("FIRST_RUN", true)
        set(check){
            preferences.edit().putBoolean("FIRST_RUN", check).apply()
        }

    fun clearRunApp() {
        preferences.edit().clear().apply()
    }
}