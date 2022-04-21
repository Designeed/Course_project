package com.example.Sleepy.ui.settings;

import android.content.Context;
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
import com.example.Sleepy.shared.MyAnimator;
import com.example.Sleepy.shared.MyPreferences;
import com.example.Sleepy.shared.MyVibrator;
import com.example.Sleepy.databinding.FragmentSettingsBinding;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.shawnlin.numberpicker.NumberPicker;

import java.util.Objects;

public class SettingsFragment extends Fragment {

    private SettingsViewModel settingsViewModel;
    private FragmentSettingsBinding binding;
    private AudioManager amAlarm;
    private Handler handler;
    private MyPreferences.SettingsApp prefs;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        settingsViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        init();
        MyAnimator.setFadeAnimation(root);

        binding.bSettingsDefault.setOnClickListener(view -> new MaterialAlertDialogBuilder(Objects.requireNonNull(getContext()))
                .setTitle(getString(R.string.title_alert_setting))
                .setMessage(getString(R.string.message_alert_setting))
                .setNegativeButton(getString(R.string.neg_b_alert_setting), (dialogInterface, i) -> dialogInterface.cancel())
                .setPositiveButton(getString(R.string.pos_b_alert_setting), (dialogInterface, i) -> {
                    ClearSharedPreferences();
                    init();
                    Snackbar.make(binding.clSettingsBg, getString(R.string.settings_clear), Snackbar.LENGTH_SHORT).show();
                })
                .show());

        binding.sSleepTime.setOnClickListener(view -> {
            binding.npTimeSleep.setEnabled( binding.sSleepTime.isChecked());
            try{
                MyVibrator.vibrate(30, getContext());
                prefs.setCheckedSleepTime(binding.sSleepTime.isChecked());
                binding.npTimeSleep.setValue(0);
                prefs.setSleepTime(0);
            }catch(Exception ex){
                errorPlay();
            }
        });

        binding.rgTheme.setOnCheckedChangeListener((radioGroup, i) -> {
            try{
                MyVibrator.vibrate(30, getContext());
                switch (i){
                    case R.id.rbThemeAuto:
                        prefs.setThemeId(R.id.rbThemeAuto);
                        break;
                    case R.id.rbThemeDark:
                        prefs.setThemeId(R.id.rbThemeDark);
                        break;
                    case R.id.rbThemePurple:
                        prefs.setThemeId(R.id.rbThemePurple);
                        break;
                }
                Objects.requireNonNull(getActivity()).recreate();
            }catch(Exception ex){
                errorPlay();
            }
        });

        binding.sAnimations.setOnClickListener(view -> {
            try{
                MyVibrator.vibrate(30, getContext());
                prefs.setAnimated(binding.sAnimations.isChecked());
                Objects.requireNonNull(getActivity()).recreate();
            }catch(Exception ex){
                errorPlay();
            }
        });

        binding.npCycles.setOnValueChangedListener((picker, oldVal, newVal) -> {
            try{
                MyVibrator.vibrate(15, getContext());
                prefs.setCardCount(newVal);
            }catch(Exception ex){
                errorPlay();
            }
        });

        binding.npDurationCycle.setOnValueChangedListener((picker, oldVal, newVal) -> {
            try{
                MyVibrator.vibrate(10, getContext());
                prefs.setCycleDuration(newVal);
            }catch(Exception ex){
                errorPlay();
            }
        });

        binding.npTimeSleep.setOnValueChangedListener((picker, oldVal, newVal) -> {
            try{
                MyVibrator.vibrate(15, getContext());
                if (binding.sSleepTime.isChecked()) prefs.setSleepTime(newVal);
                else prefs.setSleepTime(0);
            }catch(Exception ex){
                errorPlay();
            }
        });

        binding.sTimeFormat.setOnClickListener(view -> {
            try{
                MyVibrator.vibrate(30, getContext());
                prefs.set24TimeFormat(binding.sTimeFormat.isChecked());
            }catch(Exception ex){
                errorPlay();
            }
        });

        binding.sQuoteShow.setOnClickListener(view -> {
            try{
                MyVibrator.vibrate(30, getContext());
                prefs.setCheckedQuotes(binding.sQuoteShow.isChecked());
            }catch (Exception ex){
                errorPlay();
            }
        });

        binding.sVolAlarm.addOnChangeListener((slider, value, fromUser) -> {
            try {
                MyVibrator.vibrate(30, getContext());
                amAlarm.setStreamVolume(AudioManager.STREAM_ALARM, (int)value, 0);
            }catch (Exception ex){
                errorPlay();
            }
        });

        binding.sChoiceAlarm.setOnClickListener(view -> {
            try{
                MyVibrator.vibrate(30, getContext());
                prefs.setCheckedBuiltinAlarm(binding.sChoiceAlarm.isChecked());
            }catch (Exception ex){
                errorPlay();
            }
        });

        binding.ivSettingsInfo.setOnClickListener(view -> {
            MyVibrator.vibrate(30, getContext());
            if(BottomSheetBehavior.from(binding.incBottomSheet.flBottomSheet).getState() == BottomSheetBehavior.STATE_COLLAPSED){
                BottomSheetBehavior.from(binding.incBottomSheet.flBottomSheet).setState(BottomSheetBehavior.STATE_EXPANDED);
            }else{
                BottomSheetBehavior.from(binding.incBottomSheet.flBottomSheet).setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

        binding.srlUpdate.setOnRefreshListener(() -> {
           init();
           MyVibrator.vibrate(30, getContext());
           binding.srlUpdate.setRefreshing(false);
        });

        binding.sVibration.setOnClickListener(view -> {
            try{
                MyVibrator.vibrate(30, getContext());
                prefs.setVibrated(binding.sVibration.isChecked());
            }catch (Exception ex){
                errorPlay();
            }
        });

        binding.npExtraTime.setOnValueChangedListener((picker, oldVal, newVal) -> {
            try{
                MyVibrator.vibrate(15, getContext());
                prefs.setExtraTime(newVal);
            }catch(Exception ex){
                errorPlay();
            }
        });

        return root;
    }

    private void init() {
        amAlarm = (AudioManager) Objects.requireNonNull(getContext()).getSystemService(Context.AUDIO_SERVICE);
        binding.sVolAlarm.setValue(amAlarm.getStreamVolume(AudioManager.STREAM_ALARM));
        BottomSheetBehavior.from(binding.incBottomSheet.flBottomSheet).setState(BottomSheetBehavior.STATE_COLLAPSED);
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
        prefs = new MyPreferences.SettingsApp(requireContext());
        try{
            binding.rgTheme.check(prefs.getThemeId());
            binding.sAnimations.setChecked(prefs.isAnimated());
            binding.npCycles.setValue(prefs.getCardCount());
            binding.sSleepTime.setChecked(prefs.isCheckedSleepTime());
            binding.npTimeSleep.setEnabled(binding.sSleepTime.isChecked());
            binding.npTimeSleep.setValue(prefs.getSleepTime());
            binding.sTimeFormat.setChecked(prefs.is24TimeFormat());
            binding.npDurationCycle.setValue(prefs.getCycleDuration());
            binding.sQuoteShow.setChecked(prefs.isCheckedQuotes());
            binding.sChoiceAlarm.setChecked(prefs.isBuiltinAlarm());
            binding.sVibration.setChecked(prefs.isVibrated());
            binding.npExtraTime.setValue(prefs.getExtraTime());
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
        prefs.clearSettingsApp();
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