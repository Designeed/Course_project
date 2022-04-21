package com.example.Sleepy.shared;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatDelegate;

import com.example.Sleepy.R;

public class AppTheme {
    @SuppressLint("NonConstantResourceId")
    public static void setShareTheme(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("SETTINGS", Context.MODE_PRIVATE);
        switch (preferences.getInt("THEME", R.id.rbThemeAuto)) {
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
