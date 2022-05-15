package com.example.sleepy.utils

import androidx.appcompat.app.AppCompatActivity
import android.app.AlarmManager
import com.example.sleepy.utils.MyPreferences.SettingsApp
import android.app.AlarmManager.AlarmClockInfo
import com.google.android.material.snackbar.Snackbar
import com.example.sleepy.R
import android.app.PendingIntent
import android.content.Intent
import com.example.sleepy.presentation.MainActivity
import com.example.sleepy.presentation.alarm.alarmInfo.AlarmActivity
import android.content.Context
import android.provider.AlarmClock
import android.util.Log
import android.view.View
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class MyAlarm : AppCompatActivity() {
    companion object{
        var sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        private lateinit var alarmManager: AlarmManager

        fun setAlarm(context: Context, time: Calendar, view: View) {
            if (SettingsApp(context).isBuiltinAlarm) {
                if (time.before(Calendar.getInstance())) {
                    time.add(Calendar.DATE, 1)
                }

                alarmManager = view.context.getSystemService(ALARM_SERVICE) as AlarmManager
                val alarmClockInfo = AlarmClockInfo(time.timeInMillis, getAlarmInfoPendingIntent(view))
                alarmManager.setAlarmClock(alarmClockInfo, getAlarmActionPendingIntent(view))

                printInfo(view, time)
            } else {
                setAlarmInApp(time, context, view)
            }
        }

        private fun printInfo(view: View, time: Calendar) {
            Log.i("alarm", "Alarm SET " + sdf.format(time.time))
            Snackbar.make(
                view,
                view.context.getString(R.string.alarm_set_for_remaining_time,
                    sdf.format(time.time),
                    MyTimer.calcRemainingTimeMinute(
                        Date(alarmManager.nextAlarmClock.triggerTime),
                        view.context
                    )
                ),
                Snackbar.LENGTH_LONG
            )
                .setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE)
                .show()
        }

        private fun getAlarmInfoPendingIntent(view: View): PendingIntent {
            val alarmInfoIntent = Intent(view.context, MainActivity::class.java)
                .setFlags(
                    Intent.FLAG_ACTIVITY_CLEAR_TOP
                            or Intent.FLAG_ACTIVITY_NEW_TASK
                            or Intent.FLAG_ACTIVITY_CLEAR_TASK
                )
            Log.i("alarm", "Alarm INFO")

            return PendingIntent.getActivity(
                view.context,
                0,
                alarmInfoIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }

        private fun getAlarmActionPendingIntent(view: View): PendingIntent {
            val alarmInfoIntent = Intent(view.context, AlarmActivity::class.java)
                .setFlags(
                    Intent.FLAG_ACTIVITY_CLEAR_TOP
                            or Intent.FLAG_ACTIVITY_NEW_TASK
                            or Intent.FLAG_ACTIVITY_CLEAR_TASK
                )
            Log.i("alarm", "Alarm START")

            return PendingIntent.getActivity(
                view.context,
                1,
                alarmInfoIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }

        fun cancelAlarm(view: View) {
            try {
                alarmManager.cancel(getAlarmActionPendingIntent(view))
            } catch (ex: Exception) {
                Log.e("ERROR", "Error cancel alarm $ex")
            }
        }

        private fun setAlarmInApp(time: Calendar, context: Context, view: View) {
            try {
                context.startActivity(
                    Intent(AlarmClock.ACTION_SET_ALARM)
                        .putExtra(AlarmClock.EXTRA_MESSAGE, "New Alarm")
                        .putExtra(AlarmClock.EXTRA_HOUR, time[Calendar.HOUR_OF_DAY])
                        .putExtra(AlarmClock.EXTRA_MINUTES, time[Calendar.MINUTE])
                )
            } catch (ex: Exception) {
                Snackbar.make(
                    view,
                    context.getString(R.string.error_open_alarm),
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }
}