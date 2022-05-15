package com.example.sleepy.presentation.splash

import androidx.appcompat.app.AppCompatActivity
import com.example.sleepy.utils.MyPreferences.RunApp
import android.os.Bundle
import com.example.sleepy.R
import android.media.AudioManager
import android.content.Intent
import android.util.Log
import com.example.sleepy.presentation.MainActivity
import com.example.sleepy.presentation.onBoarding.OnBoardingActivity
import com.example.sleepy.utils.AppTheme

class SplashActivity : AppCompatActivity() {
    private lateinit var run: RunApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppTheme.setShareTheme(this)
        setContentView(R.layout.activity_splash)
        init()
        checkRun()
    }

    private fun init() {
        volumeControlStream = AudioManager.STREAM_ALARM
        run = RunApp(this)
    }

    private fun checkRun() {
        if (run.isFirstRun) {
            startActivity(Intent(this, OnBoardingActivity::class.java))
            finish()
            run.isFirstRun = false
            Log.i("run", "first")
        } else {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            Log.i("run", "not first")
        }
    }
}