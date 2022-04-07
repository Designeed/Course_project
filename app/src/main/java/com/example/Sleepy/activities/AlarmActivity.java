package com.example.Sleepy.activities;

import static android.media.AudioManager.STREAM_ALARM;
import static android.media.RingtoneManager.TYPE_ALARM;
import static android.media.RingtoneManager.TYPE_NOTIFICATION;
import static android.media.RingtoneManager.TYPE_RINGTONE;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.example.Sleepy.R;
import com.example.Sleepy.classes.Quotes;
import com.example.Sleepy.databinding.ActivityAlarmBinding;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class AlarmActivity extends AppCompatActivity {

    private ActivityAlarmBinding binding;
    private MediaPlayer mpRingtone = new MediaPlayer();
    private Ringtone ringtone;
    private Uri notificationUri;
    private PowerManager.WakeLock mWakeLock;
    private AudioAttributes aaAlarmType;
    private boolean quote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        binding = ActivityAlarmBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
        playSound();

        binding.bStopAlarm.setOnClickListener(view -> {
            stopAlarm();
            startActivity(new Intent(getApplicationContext(), SplashActivity.class));
            finish();
        });
    }

    private void init(){
        Objects.requireNonNull(getSupportActionBar()).hide();
        setVolumeControlStream(STREAM_ALARM);
        setAlarmType();
        getShared();
        setQuoteAndTime();
    }

    private void setAlarmType() {
        aaAlarmType = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ALARM)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build();
        getNotificationUriAlarm();
    }

    private void getNotificationUriAlarm() {
        notificationUri = RingtoneManager.getActualDefaultRingtoneUri(getApplicationContext(), TYPE_ALARM);
        Log.i("alarm", "type_alarm");
        if(notificationUri == null){
            notificationUri = RingtoneManager.getActualDefaultRingtoneUri(getApplicationContext(), TYPE_RINGTONE);
            Log.i("alarm", "type_ringtone");
            if(notificationUri == null){
                notificationUri = RingtoneManager.getActualDefaultRingtoneUri(getApplicationContext(), TYPE_NOTIFICATION);
                Log.i("alarm", "type_notification");
            }
        }
        setScreenFlags();
    }

    private void playSound() {
        try{
            if(notificationUri != null){
                ringtone = RingtoneManager.getRingtone(getApplicationContext(), notificationUri);
                ringtone.setAudioAttributes(aaAlarmType);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    ringtone.setLooping(true);
                }
                ringtone.play();
            }

            if(!ringtone.isPlaying() && notificationUri != null) {
                mpRingtone = MediaPlayer.create(getApplicationContext(), notificationUri);
                try{
                    mpRingtone.setAudioAttributes(aaAlarmType);
                }catch (Exception ex){
                    Log.i("alarm", "setAttributesMethod is null - " + ex);
                }
                mpRingtone.setLooping(true);
                mpRingtone.start();
                Log.i("alarm", "Start media Playing :|");
            }else{
                Log.i("alarm", "Start ringtone Playing :)");
            }
        }catch (Exception ex){
            finish();
            Log.i("alarm", "ringtone cannot play :( " + ex);
        }
    }

    private void setScreenFlags() {
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "Sleepy:alarmScreen");
        mWakeLock.acquire(10*60*1000L /*10 minutes*/);

        final Window win = getWindow();
        win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON);
    }

    private void setQuoteAndTime() {
        binding.tvAlarmTime.setText(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date().getTime()));

        if(quote){
            binding.tvAlarmInfo.setText(Quotes.getQuoteAlarm());
        }else{
            binding.tvAlarmInfo.setText("Будильник");
        }
    }

    private void getShared() {
        SharedPreferences prefs = getSharedPreferences("SETTINGS", MODE_PRIVATE);
        quote = prefs.getBoolean("QUOTE", true);
    }

    @Override
    protected void onDestroy() {
        stopAlarm();
        startActivity(new Intent(this, SplashActivity.class));
        super.onDestroy();
    }

    private void stopAlarm(){
        if (mpRingtone != null && mpRingtone.isPlaying()) {
            mpRingtone.stop();
            Log.i("alarm", "Stop media Playing :|");
        }
        if(ringtone != null && ringtone.isPlaying()){
            ringtone.stop();
            Log.i("alarm", "Stop ringtone Playing :)");
        }
        if (mWakeLock.isHeld()) {
            mWakeLock.release();
        }
    }
}