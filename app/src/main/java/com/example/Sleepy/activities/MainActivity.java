package com.example.Sleepy.activities;

import static android.media.AudioManager.STREAM_ALARM;
import static android.media.AudioManager.STREAM_RING;

import android.os.Bundle;
import android.view.Menu;

import com.example.Sleepy.R;
import com.example.Sleepy.classes.AppTheme;
import com.google.android.material.navigation.NavigationView;

import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import com.example.Sleepy.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        AppTheme.setShareTheme(getApplicationContext());
        setContentView(binding.getRoot());

        init();
    }

    private void init(){
        setVolumeControlStream(STREAM_ALARM);
        setSupportActionBar(binding.appBarMain.toolbar);
        setNavConfig();
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