package com.example.sleepy.data.storage

import android.content.Context
import android.content.SharedPreferences
import com.example.sleepy.utils.Constants

class RunStorage(context: Context) {
    private val preferences: SharedPreferences =
        context.getSharedPreferences(Constants.RUN_PREFS_NAME, Context.MODE_PRIVATE)

    var isFirstRun: Boolean
        get() = preferences.getBoolean(Constants.FIRST_RUN_KEY, true)
        set(check){
            preferences.edit().putBoolean(Constants.FIRST_RUN_KEY, check).apply()
        }

    fun clearRunApp() {
        preferences.edit().clear().apply()
    }
}