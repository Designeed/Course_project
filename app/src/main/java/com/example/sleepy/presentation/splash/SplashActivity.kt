package com.example.sleepy.presentation.splash

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Intent
import android.content.res.Configuration
import android.media.AudioManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.animation.doOnEnd
import com.example.sleepy.R
import com.example.sleepy.data.storage.PrefsStorage
import com.example.sleepy.data.storage.RunStorage
import com.example.sleepy.presentation.MainActivity
import com.example.sleepy.presentation.onBoarding.OnBoardingActivity
import com.example.sleepy.utils.AppTheme

class SplashActivity : AppCompatActivity() {
    private lateinit var run: RunStorage
    private lateinit var prefs: PrefsStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppTheme.setShareTheme(this)
        setContentView(R.layout.activity_splash)
        init()
        supportActionBar?.hide()
        animateLogo()
    }

    private fun init() {
        volumeControlStream = AudioManager.STREAM_ALARM
        run = RunStorage(this)
    }

    private fun checkRun() {
        if (run.isFirstRun) {
            startActivity(Intent(this, OnBoardingActivity::class.java))
            finish()
            overridePendingTransition(R.anim.enter_alpha, R.anim.exit_alpha)
            run.isFirstRun = false
            Log.i("run", "first")
        } else {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            overridePendingTransition(R.anim.enter_alpha, R.anim.exit_alpha)
            Log.i("run", "not first")
        }
    }

    private fun animateLogo(){
        prefs = PrefsStorage(this)
        val ivLogo = findViewById<ImageView>(R.id.ivSplashLogo)
        ivLogo.visibility = View.VISIBLE

        val anim = ivLogo.animate()
            .setDuration(600)
            .scaleX(0f)
            .scaleY(0f)
            .scaleXBy(0.2f)
            .scaleYBy(0.2f)
            .withStartAction {
                val anim2 = ValueAnimator
                    .ofObject(
                        ArgbEvaluator(),
                        getColor(R.color.purple_200),
                        getThemeColor()
                    )
                    .setDuration(1000)

                anim2.addUpdateListener { animator ->
                    findViewById<ConstraintLayout>(R.id.clMainSplash).setBackgroundColor(animator.animatedValue as Int)
                }

                anim2.start()

                anim2.doOnEnd { checkRun() }
            }
    }

    private fun getThemeColor(): Int{
        var colorRes = getColor(R.color.bg_p)
        when (prefs.themeId) {
            R.id.rbThemePurple -> colorRes = getColor(R.color.bg_p)
            R.id.rbThemeDark -> colorRes = getColor(R.color.bg_d)
            R.id.rbThemeAuto -> {
                when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                    Configuration.UI_MODE_NIGHT_NO -> colorRes = getColor(R.color.bg_p)
                    Configuration.UI_MODE_NIGHT_YES -> colorRes = getColor(R.color.bg_d)
                }
            }
        }
        return colorRes
    }
}