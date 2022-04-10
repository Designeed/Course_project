package com.example.Sleepy.activities;

import static android.media.AudioManager.STREAM_ALARM;
import static android.media.AudioManager.STREAM_RING;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.example.Sleepy.R;
import com.example.Sleepy.classes.AppTheme;

public class SplashActivity extends AppCompatActivity {

    SharedPreferences prefs = null;
    boolean isFirstRun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppTheme.setShareTheme(getApplicationContext());
        setContentView(R.layout.activity_splash);

        init();
        checkRun();
    }

    private void init(){
        setVolumeControlStream(STREAM_ALARM);
        getShared();
    }

    private void checkRun() {
        if(isFirstRun){
            startActivity(new Intent(this, DemoActivity.class));
            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
            finish();
            prefs.edit().putBoolean("FIRST_RUN", false).apply();
            Log.i("run","first");
        }else{
            startActivity(new Intent(this, MainActivity.class));
            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
            finish();
            Log.i("run","not first");
        }
    }

    private void getShared() {
        prefs = getSharedPreferences("SETTINGS_RUN", Context.MODE_PRIVATE);
        isFirstRun = prefs.getBoolean("FIRST_RUN", true);
    }
}