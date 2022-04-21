package com.example.Sleepy.shared;

import android.content.Context;

import androidx.appcompat.app.AppCompatDelegate;

import com.example.Sleepy.R;

public class AppTheme {
    public static void setShareTheme(Context context) {
        MyPreferences.SettingsApp prefs = new MyPreferences.SettingsApp(context);
        switch (prefs.getThemeId()) {
            case R.id.rbThemePurple:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case R.id.rbThemeDark:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            case R.id.rbThemeAuto:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                break;
        }
    }
}
