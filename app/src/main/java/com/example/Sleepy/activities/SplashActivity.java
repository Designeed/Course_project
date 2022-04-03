package com.example.Sleepy.activities;

import static android.media.AudioManager.STREAM_RING;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.example.Sleepy.R;
import com.example.Sleepy.classes.AppTheme;

public class SplashActivity extends AppCompatActivity {

    SharedPreferences prefs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppTheme.setShareTheme(getApplicationContext());
        setContentView(R.layout.activity_splash);

        setVolumeControlStream(STREAM_RING);

        prefs = getSharedPreferences("SETTINGS_RUN", Context.MODE_PRIVATE);

        if(prefs.getBoolean("FIRST_RUN", true)){
            startActivity(new Intent(this, DemoActivity.class));
            finish();
            prefs.edit().putBoolean("FIRST_RUN", false).apply();
            Log.i("run","" + prefs.getBoolean("FIRST_RUN", true));
        }else{
            startActivity(new Intent(this, MainActivity.class));
            finish();
            Log.i("run","" + prefs.getBoolean("FIRST_RUN", false));
        }
        overridePendingTransition(R.anim.nav_default_enter_anim, R.anim.nav_default_exit_anim);

    }
}