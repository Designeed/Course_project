package com.example.sleepy.presentation

import android.media.AudioManager
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.navigateUp
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.airbnb.lottie.LottieAnimationView
import com.example.sleepy.R
import com.example.sleepy.data.storage.PrefsStorage
import com.example.sleepy.databinding.ActivityMainBinding
import com.example.sleepy.utils.AppTheme
import com.example.sleepy.utils.AnimationsUtils


class MainActivity : AppCompatActivity() {
    private lateinit var mAppBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var lStar: LottieAnimationView
    private lateinit var lLogo: LottieAnimationView
    private var isAnimate = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        AppTheme.setShareTheme(applicationContext)
        setContentView(binding.root)
        init()
        AnimationsUtils.setFadeAnimationStart(binding.root)
    }

    private fun init() {
        volumeControlStream = AudioManager.STREAM_ALARM
        setSupportActionBar(binding.appBarMain.toolbar)
        lLogo = binding.navView.getHeaderView(0).findViewById(R.id.lLogo)
        lStar = binding.navView.getHeaderView(0).findViewById(R.id.lMenuBg)
        setNavConfig()
        shared
        setAnimation()
    }

    private val shared: Unit
        get() {
            val prefs = PrefsStorage(this)
            isAnimate = prefs.isAnimated
        }

    private fun setAnimation() {
        if (!isAnimate) {
            lLogo.speed = 0f
            lStar.speed = 0f
        } else {
            lStar.speed = 1f
            lLogo.speed = 1f
        }
    }

    private fun setNavConfig() {
        mAppBarConfiguration = AppBarConfiguration.Builder(
            R.id.nav_sleep,
            R.id.nav_wake,
            R.id.nav_alarm,
            R.id.nav_settings
        )
            .setOpenableLayout(binding.drawerLayout)
            .build()
        val navController = findNavController(this, R.id.nav_host_fragment_content_main)
        setupActionBarWithNavController(this, navController, mAppBarConfiguration)
        setupWithNavController(binding.navView, navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(this, R.id.nav_host_fragment_content_main)
        return (navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp())
    }//todo swipe

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START))
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        else super.onBackPressed()
    }
}