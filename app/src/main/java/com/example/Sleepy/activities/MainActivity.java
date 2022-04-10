package com.example.Sleepy.activities;

import static android.media.AudioManager.STREAM_ALARM;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;

import com.airbnb.lottie.LottieAnimationView;
import com.example.Sleepy.R;
import com.example.Sleepy.classes.AppTheme;

import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.appcompat.app.AppCompatActivity;

import com.example.Sleepy.classes.MyAnimator;
import com.example.Sleepy.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    LottieAnimationView lStar, lLogo;
    boolean isAnimate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        AppTheme.setShareTheme(getApplicationContext());
        setContentView(binding.getRoot());

        init();
        MyAnimator.setFadeAnimation(binding.getRoot());
    }

    private void init(){
        setVolumeControlStream(STREAM_ALARM);
        setSupportActionBar(binding.appBarMain.toolbar);
        lLogo = binding.navView.getHeaderView(0).findViewById(R.id.lLogo);
        lStar = binding.navView.getHeaderView(0).findViewById(R.id.lMenuBg);
        setNavConfig();
        getShared();
        setAnimation();
    }

    private void getShared() {
        SharedPreferences prefs = (SharedPreferences) getApplicationContext().getSharedPreferences("SETTINGS", Context.MODE_PRIVATE);
        isAnimate = prefs.getBoolean("ANIMATIONS", true);
    }

    private void setAnimation(){
        if (!isAnimate){
            lLogo.setSpeed(0);
            lStar.setSpeed(0);
        }else{
            lStar.setSpeed(1);
            lLogo.setSpeed(1);
        }
    }

    private void setNavConfig() {
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_sleep, R.id.nav_wake, R.id.nav_alarm, R.id.nav_settings)
                .setOpenableLayout(binding.drawerLayout)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        if(binding.drawerLayout.isDrawerOpen(GravityCompat.START)) binding.drawerLayout.closeDrawer(GravityCompat.START);
        else super.onBackPressed();
    }

    public void setTitleAppBar(String title){
        binding.appBarMain.toolbar.setSubtitle(title);
    }
}