package com.example.Sleepy.ui.splash;

import static android.media.AudioManager.STREAM_ALARM;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.Sleepy.R;
import com.example.Sleepy.core.presentation.MainActivity;
import com.example.Sleepy.main.modules.onBoarding.presentation.OnBoardingActivity;
import com.example.Sleepy.shared.AppTheme;
import com.example.Sleepy.shared.MyPreferences;

public class SplashActivity extends AppCompatActivity {
    MyPreferences.RunApp run;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppTheme.setShareTheme(this);
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
            startActivity(new Intent(this, OnBoardingActivity.class));
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