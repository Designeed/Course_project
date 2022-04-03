package com.example.Sleepy.activities;

import static android.media.AudioManager.STREAM_RING;
import static android.media.RingtoneManager.TYPE_ALARM;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.Sleepy.R;
import com.example.Sleepy.classes.MyVibrator;
import com.example.Sleepy.classes.QuotesAlarm;
import com.example.Sleepy.databinding.ActivityAlarmBinding;
import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class AlarmActivity extends AppCompatActivity {

    private ActivityAlarmBinding binding;
    private MaterialButton bStopAlarm;
    private TextView tvTime;
    private MediaPlayer mpRingtone = new MediaPlayer();
    private Ringtone ringtone;
    private Uri notificationUri;
    private TextView tvQuote;
    private SharedPreferences prefs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        binding = ActivityAlarmBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();

        tvTime.setText("" + new SimpleDateFormat("HH:mm").format(new Date().getTime()));

        notificationUri = RingtoneManager.getActualDefaultRingtoneUri(getApplicationContext(), TYPE_ALARM);

        ringtone = RingtoneManager.getRingtone(getApplicationContext(), notificationUri);
        ringtone.play();

        if(!ringtone.isPlaying()) {
            mpRingtone = MediaPlayer.create(getApplicationContext(), notificationUri);
            mpRingtone.setLooping(true);
            mpRingtone.start();
            Log.i("alarm_ringtone", "Start media Playing");
        }else{
            Log.i("alarm_ringtone", "Start ringtone Playing");
        }

        bStopAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopAlarm();
                startActivity(new Intent(getApplicationContext(), SplashActivity.class));
                finish();
            }
        });
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
            Log.i("alarm_ringtone", "Stop media Playing");
        }
        if(ringtone != null && ringtone.isPlaying()){
            ringtone.stop();
            Log.i("alarm_ringtone", "Stop ringtone Playing");
        }
    }

    private void init(){
        bStopAlarm = binding.bStopAlarm;
        tvTime = binding.tvAlarmTime;
        tvQuote = binding.tvAlarmInfo;
        Objects.requireNonNull(getSupportActionBar()).hide();
        prefs = getSharedPreferences("SETTINGS", MODE_PRIVATE);

        setVolumeControlStream(STREAM_RING);

        if(prefs.getBoolean("QUOTE", true)){
            tvQuote.setText(QuotesAlarm.getQuote());
        }else{
            tvQuote.setText("Будильник");
        }

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock mWakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "Sleepy:alarmScreen");
        mWakeLock.acquire(10*60*1000L /*10 minutes*/);

        final Window win = getWindow();
        win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        win.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON);
    }
}