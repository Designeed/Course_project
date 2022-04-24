package com.example.Sleepy.shared

import android.content.Context
import com.example.Sleepy.R
import androidx.appcompat.app.AppCompatDelegate

class AppTheme {
    companion object{
        fun setShareTheme(context: Context) {
            val preferences = MyPreferences.SettingsApp(context)
            when (preferences.themeId) {
                R.id.rbThemePurple -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                R.id.rbThemeDark -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                R.id.rbThemeAuto -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            }
        }
    }

}