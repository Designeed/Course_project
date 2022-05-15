package com.example.sleepy.utils

import android.content.Context
import com.example.sleepy.R
import android.widget.TimePicker
import android.view.View
import android.widget.TextView
import java.util.*

class TimeUtils {
    companion object{
        fun calcRemainingTimeMinute(date: Date, context: Context): String {
            val remTime: String
            val timeUp = date.time
            val diff = timeUp - Date().time
            val diffMinutes = diff / (60 * 1000) % 60
            val diffHours = diff / (60 * 60 * 1000)
            remTime = when {
                diffMinutes == 0L -> "" + diffHours + context.getString(R.string.hours)
                diffHours == 0L -> "" + diffMinutes + context.getString(R.string.minute)
                else -> "" + diffHours + context.getString(R.string.hours) + diffMinutes + context.getString(R.string.minute)
            }
            return remTime
        }

        fun getFormatTime(minute: Long, context: Context): String {
            val remTime: String
            val h: Long = minute / 60
            val m: Long = minute - h * 60
            remTime = when {
                m == 0L ->  "" + h + context.getString(R.string.hours)
                h != 0L && m != 0L -> "" + h + context.getString(R.string.hours) + m + context.getString(R.string.minute)
                else -> "" + m + context.getString(R.string.minute)
            }
            return remTime
        }

        fun clearTime(tp: TimePicker, context: Context) {
            VibrationUtils.vibrate(30, context)
            tp.hour = Calendar.getInstance()[Calendar.HOUR_OF_DAY]
            tp.minute = Calendar.getInstance()[Calendar.MINUTE]
        }

        fun getCurrentTime(tp: TimePicker): Calendar {
            val timeAlarm = Calendar.getInstance()
            timeAlarm[Calendar.MINUTE] = tp.minute
            timeAlarm[Calendar.HOUR_OF_DAY] = tp.hour
            return timeAlarm
        }

        fun getAsleepText(asleepTime: Int, tv: TextView, context: Context) {
            if (asleepTime != 0) {
                tv.visibility = View.VISIBLE
                tv.text = context.getString(R.string.calculate_with_sleep_time, asleepTime)
            } else {
                tv.visibility = View.GONE
            }
        }
    }
}