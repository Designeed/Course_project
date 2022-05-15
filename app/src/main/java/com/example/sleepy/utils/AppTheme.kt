package com.example.sleepy.utils

import android.content.Context
import com.example.sleepy.R
import androidx.appcompat.app.AppCompatDelegate
import com.example.sleepy.data.storage.PrefsStorage

class AppTheme {
    companion object{
        fun setShareTheme(context: Context) {
            val preferences = PrefsStorage(context)
            when (preferences.themeId) {
                R.id.rbThemePurple -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                R.id.rbThemeDark -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                R.id.rbThemeAuto -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            }
        }
    }

}