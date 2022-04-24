package com.example.Sleepy.shared

import android.content.Context
import com.example.Sleepy.shared.MyPreferences.SettingsApp
import android.os.Build
import android.os.Vibrator
import android.os.VibrationEffect
import androidx.core.content.getSystemService

class MyVibrator {
    companion object{
        fun vibrate(ms: Long, context: Context) {
            if (SettingsApp(context).isVibrated) {
                if (Build.VERSION.SDK_INT >= 26) {
                    (context.getSystemService<Vibrator>())
                        ?.vibrate(VibrationEffect.createOneShot(ms, VibrationEffect.DEFAULT_AMPLITUDE))
                } else {
                    (context.getSystemService<Vibrator>())?.vibrate(ms)
                }
            }
        }

        fun vibrateAlarm(context: Context, repeat: Int) {
            (context.getSystemService<Vibrator>())?.vibrate(
                longArrayOf(
                    1000,
                    1000,
                    1000,
                    1000,
                    1000
                ), repeat
            )
        } //TODO реализовать
    }
}