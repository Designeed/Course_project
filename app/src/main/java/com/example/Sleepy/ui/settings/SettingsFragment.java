package com.example.Sleepy.ui.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.Sleepy.R;
import com.example.Sleepy.databinding.FragmentSettingsBinding;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

public class SettingsFragment extends Fragment {

    private SettingsViewModel settingsViewModel;
    private FragmentSettingsBinding binding;
    private SharedPreferences prefs = null;
    private AudioManager amAlarm;
    Handler handler;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        settingsViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        init();

        binding.bSettingsDefault.setOnClickListener(view -> new MaterialAlertDialogBuilder(Objects.requireNonNull(getContext()))
                .setTitle(getString(R.string.title_alert_setting))
                .setMessage(getString(R.string.message_alert_setting))
                .setNegativeButton(getString(R.string.neg_b_alert_setting), (dialogInterface, i) -> dialogInterface.cancel())
                .setPositiveButton(getString(R.string.pos_b_alert_setting), (dialogInterface, i) -> {
                    ClearSharedPreferences();
                    init();
                    Snackbar.make(binding.clSettingsBg, "Настройки сброшены", Snackbar.LENGTH_SHORT).show();
                })
                .show());

        binding.sSleepTime.setOnClickListener(view -> {
            binding.npTimeSleep.setEnabled( binding.sSleepTime.isChecked());
            try{
                prefs.edit().putBoolean("CHECK_SLEEP",  binding.sSleepTime.isChecked()).apply();
                binding.npTimeSleep.setValue(0);
                prefs.edit().putInt("SLEEP_TIME", 0).apply();
            }catch(Exception ex){
                errorPlay();
            }
        });

        binding.rgTheme.setOnCheckedChangeListener((radioGroup, i) -> {
            try{
                switch (i){
                    case R.id.rbThemeAuto:
                        prefs.edit().putInt("THEME", R.id.rbThemeAuto).apply();
                        break;
                    case R.id.rbThemeDark:
                        prefs.edit().putInt("THEME", R.id.rbThemeDark).apply();
                        break;
                    case R.id.rbThemePurple:
                        prefs.edit().putInt("THEME", R.id.rbThemePurple).apply();
                        break;
                }
                Objects.requireNonNull(getActivity()).recreate();
            }catch(Exception ex){
                errorPlay();
            }
        });

        binding.sAnimations.setOnClickListener(view -> {
            try{
                prefs.edit().putBoolean("ANIMATIONS", binding.sAnimations.isChecked()).apply();
            }catch(Exception ex){
                errorPlay();
            }
        });

        binding.npCycles.setOnValueChangedListener((picker, oldVal, newVal) -> {
            try{
                prefs.edit().putInt("CARD_COUNT", newVal).apply();
            }catch(Exception ex){
                errorPlay();
            }
        });

        binding.npDurationCycle.setOnValueChangedListener((picker, oldVal, newVal) -> {
            try{
                prefs.edit().putInt("CYCLE_DURATION", newVal).apply();
            }catch(Exception ex){
                errorPlay();
            }
        });

        binding.npTimeSleep.setOnValueChangedListener((picker, oldVal, newVal) -> {
            try{
                if (binding.sSleepTime.isChecked()) prefs.edit().putInt("SLEEP_TIME", newVal).apply();
                else prefs.edit().putInt("SLEEP_TIME", 0).apply();
            }catch(Exception ex){
                errorPlay();
            }
        });

        binding.sTimeFormat.setOnClickListener(view -> {
            try{
                prefs.edit().putBoolean("TIME_FORMAT", binding.sTimeFormat.isChecked()).apply();
            }catch(Exception ex){
                errorPlay();
            }
        });

        binding.sQuoteShow.setOnClickListener(view -> {
            try{
                prefs.edit().putBoolean("QUOTE", binding.sQuoteShow.isChecked()).apply();
            }catch (Exception ex){
                errorPlay();
            }
        });

        binding.sVolAlarm.addOnChangeListener((slider, value, fromUser) -> {
            try {
                amAlarm.setStreamVolume(AudioManager.STREAM_ALARM, (int)value, 0);
            }catch (Exception ex){
                errorPlay();
            }
        });

        binding.sChoiceAlarm.setOnClickListener(view -> {
            try{
                prefs.edit().putBoolean("WHAT_ALARM", binding.sChoiceAlarm.isChecked()).apply();
            }catch (Exception ex){
                errorPlay();
            }
        });

        return root;
    }

    private void init() {
        amAlarm = (AudioManager) Objects.requireNonNull(getContext()).getSystemService(Context.AUDIO_SERVICE);
        binding.sVolAlarm.setValue(amAlarm.getStreamVolume(AudioManager.STREAM_ALARM));
        handler = new Handler();
        handler.removeCallbacks(run);
        handler.postDelayed(run, 100);

        getShared();
        setVolumeAlarm();
    }

    private void setVolumeAlarm() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            binding.sVolAlarm.setValueFrom(amAlarm.getStreamMinVolume(AudioManager.STREAM_ALARM));
        }
        binding.sVolAlarm.setValueTo(amAlarm.getStreamMaxVolume(AudioManager.STREAM_ALARM));
    }

    private void loadPlay() {
        binding.lCheckLoad.setAnimation(R.raw.check_mark_settings);
        if(!binding.lCheckLoad.isAnimating()) binding.lCheckLoad.playAnimation();
    }

    private void errorPlay(){
        binding.lCheckLoad.setAnimation(R.raw.exclamation_mark);
        if(!binding.lCheckLoad.isAnimating()) binding.lCheckLoad.playAnimation();
    }

    private void getShared() {
        prefs = Objects.requireNonNull(getContext()).getSharedPreferences("SETTINGS", Context.MODE_PRIVATE);
        try{
            binding.rgTheme.check(prefs.getInt("THEME", R.id.rbThemeAuto));
            binding.sAnimations.setChecked(prefs.getBoolean("ANIMATIONS", true));
            binding.npCycles.setValue(prefs.getInt("CARD_COUNT", 6));
            binding.sSleepTime.setChecked(prefs.getBoolean("CHECK_SLEEP", false));
            binding.npTimeSleep.setEnabled(binding.sSleepTime.isChecked());
            binding.npTimeSleep.setValue(prefs.getInt("SLEEP_TIME", 0));
            binding.sTimeFormat.setChecked(prefs.getBoolean("TIME_FORMAT", true));
            binding.npDurationCycle.setValue(prefs.getInt("CYCLE_DURATION", 90));
            binding.sQuoteShow.setChecked(prefs.getBoolean("QUOTE", true));
            binding.sChoiceAlarm.setChecked(prefs.getBoolean("WHAT_ALARM", true));
            loadPlay();
            Log.i("LOADING_SETTINGS", "Ok");
        }catch (Exception ex){
            errorPlay();
            Log.i("LOADING_SETTINGS", "init_error");
        }
    }

    private final Runnable run = new Runnable() {
        @Override
        public void run() {
            try{
                binding.sVolAlarm.setValue(amAlarm.getStreamVolume(AudioManager.STREAM_ALARM));
            }catch (Exception ex){
                Log.i("SETTINGS", "setVolume" + ex);
            }
            handler.postDelayed(this, 1000);
        }
    };

    private void ClearSharedPreferences(){
        prefs.edit().clear().apply();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onPause() {
        handler.removeCallbacks(run);
        super.onPause();
    }

    @Override
    public void onResume() {
        handler.postDelayed(run, 100);
        super.onResume();
    }
}