package com.example.Sleepy.ui.splash

import androidx.appcompat.app.AppCompatActivity
import com.example.Sleepy.shared.MyPreferences.RunApp
import android.os.Bundle
import com.example.Sleepy.R
import android.media.AudioManager
import android.content.Intent
import android.util.Log
import com.example.Sleepy.main.modules.onBoarding.presentation.OnBoardingActivity
import com.example.Sleepy.core.presentation.MainActivity
import com.example.Sleepy.shared.AppTheme

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
            run.setCheckedRun(false)
            Log.i("run", "first")
        } else {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            Log.i("run", "not first")
        }
    }
}