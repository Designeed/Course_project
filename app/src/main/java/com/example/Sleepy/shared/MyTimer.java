package com.example.Sleepy.shared;

import android.content.Context;
import android.content.Intent;
import android.provider.AlarmClock;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.Sleepy.R;
import com.google.android.material.snackbar.Snackbar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class MyTimer {

    public static String calcRemainingTimeMinute(Date date, Context context){
        String RemTime;
        long timeUp = date.getTime();
        long diff = timeUp - new Date().getTime();

        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000);

        if (diffMinutes == 0){
            RemTime = "" + diffHours + context.getString(R.string.hours);
        }else if(diffHours == 0){
            RemTime = "" + diffMinutes + context.getString(R.string.minute);
        }else{
            RemTime = "" + diffHours + context.getString(R.string.hours) + diffMinutes + context.getString(R.string.minute);
        }
        return RemTime;
    }

    public static String getFormatTime(long minute, Context context){
        String RemTime;
        long h, m;
        h = minute / 60;
        m = minute - h * 60;
        if (m == 0){
            RemTime = "" + h + context.getString(R.string.hours);
        }else if(h != 0 && m != 0){
            RemTime = "" + h + context.getString(R.string.hours) + m + context.getString(R.string.minute);
        }else{
            RemTime = "" + m + context.getString(R.string.minute);
        }
        return RemTime;
    }

    public static void clearTime(TimePicker tp, Context context){
        MyVibrator.vibrate(30, context);
        tp.setHour(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
        tp.setMinute(Calendar.getInstance().get(Calendar.MINUTE));
    }

    public static Calendar getCurrentTime(TimePicker tp){
        Calendar timeAlarm = Calendar.getInstance();
        timeAlarm.set(Calendar.MINUTE, tp.getMinute());
        timeAlarm.set(Calendar.HOUR_OF_DAY, tp.getHour());
        return timeAlarm;
    }

    public static void setAlarmInApp(Calendar time, Context context, View view){
        try{
            context.startActivity(new Intent(AlarmClock.ACTION_SET_ALARM)
                    .putExtra(AlarmClock.EXTRA_MESSAGE, "New Alarm")
                    .putExtra(AlarmClock.EXTRA_HOUR, time.get(Calendar.HOUR_OF_DAY))
                    .putExtra(AlarmClock.EXTRA_MINUTES, time.get(Calendar.MINUTE)));
        }catch (Exception ex){
            Snackbar.make(view, context.getString(R.string.error_open_alarm), Snackbar.LENGTH_LONG).show();
        }
    }

    public static void getAsleepText(int asleepTime, TextView tv, Context context) {
        if (asleepTime != 0){
            tv.setVisibility(View.VISIBLE);
            tv.setText(new StringBuilder()
                    .append(context.getString(R.string.calculate_with_sleep_time))
                    .append(asleepTime)
                    .append(context.getString(R.string.min)));
        }else {
            tv.setVisibility(View.GONE);
        }
    }

    public static Calendar getTimeFromText(CharSequence timeStr, String remTime, Date date){
        Calendar c = Calendar.getInstance();
        Date d = new Date();
        try {
            d = new Date(Objects.requireNonNull(new SimpleDateFormat("HH:mm", Locale.getDefault()).parse((String) timeStr)).getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        c.set(date.getYear(), date.getMonth(), date.getDate(), d.getHours(), d.getMinutes());

        return c;
    }  //todo прибавлять сутки если осталось больше 24
}
