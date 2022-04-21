package com.example.Sleepy.activities;

import static android.media.AudioManager.STREAM_ALARM;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.Sleepy.R;
import com.example.Sleepy.shared.AppTheme;
import com.example.Sleepy.shared.MyPreferences;

public class SplashActivity extends AppCompatActivity {
    MyPreferences.RunApp run;

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
        run = new MyPreferences.RunApp(this);
    }

    private void checkRun() {
        if(run.isFirstRun()){
            startActivity(new Intent(this, DemoActivity.class));
            finish();
            run.setCheckedRun(false);
            Log.i("run","first");
        }else{
            startActivity(new Intent(this, MainActivity.class));
            finish();
            Log.i("run","not first");
        }
    }
}