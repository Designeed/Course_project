package com.example.Sleepy.ui.settings;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.airbnb.lottie.LottieAnimationView;
import com.example.Sleepy.R;
import com.example.Sleepy.databinding.FragmentSettingsBinding;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.shawnlin.numberpicker.NumberPicker;

import java.util.Objects;

public class SettingsFragment extends Fragment {

    private SettingsViewModel settingsViewModel;
    private FragmentSettingsBinding binding;
    RadioGroup rgTheme;
    SwitchMaterial sAnim;
    SwitchMaterial sCheckSleep;
    SwitchMaterial sTimeFormat;
    NumberPicker npCardCount;
    NumberPicker npSleepTime;
    NumberPicker npCycleDuration;
    LottieAnimationView lCheckLoad;
    private SharedPreferences prefs = null;
    RadioButton rbAuto, rbDark, rbPurple;
    MaterialButton bSetDefault;
    RelativeLayout rlSettings;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        settingsViewModel = new ViewModelProvider(this).get(SettingsViewModel.class);
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        init();

        bSetDefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialAlertDialogBuilder(Objects.requireNonNull(getContext()))
                        .setTitle(getString(R.string.title_alert_setting))
                        .setMessage(getString(R.string.message_alert_setting))
                        .setNegativeButton(getString(R.string.neg_b_alert_setting), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        })
                        .setPositiveButton(getString(R.string.pos_b_alert_setting), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ClearSharedPreferences();
                                init();
                                Snackbar.make(rlSettings, "Настройки сброшены", Snackbar.LENGTH_SHORT).show();
                            }
                        })
                        .show();
            }
        });

        sCheckSleep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                npSleepTime.setEnabled(sCheckSleep.isChecked());
                try{
                    prefs.edit().putBoolean("CHECK_SLEEP", sCheckSleep.isChecked()).apply();
                    npSleepTime.setValue(0);
                    prefs.edit().putInt("SLEEP_TIME", 0).apply();
                }catch(Exception ex){
                    errorPlay();
                }
            }
        });

        rgTheme.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                try{
                    switch (i){
                        case R.id.rbThemePurple:
                            prefs.edit().putInt("THEME", R.id.rbThemePurple).apply();
                            break;
                        case R.id.rbThemeDark:
                            prefs.edit().putInt("THEME", R.id.rbThemeDark).apply();
                            break;
                        case R.id.rbThemeAuto:
                            prefs.edit().putInt("THEME", R.id.rbThemeAuto).apply();
                            break;
                    }
                    getActivity().recreate();
                }catch(Exception ex){
                    errorPlay();
                }
            }
        });

        sAnim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    prefs.edit().putBoolean("ANIMATIONS", sAnim.isChecked()).apply();
                }catch(Exception ex){
                    errorPlay();
                }
            }
        });

        npCardCount.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                try{
                    prefs.edit().putInt("CARD_COUNT", newVal).apply();
                }catch(Exception ex){
                    errorPlay();
                }
            }
        });

        npCycleDuration.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                try{
                    prefs.edit().putInt("CYCLE_DURATION", newVal).apply();
                }catch(Exception ex){
                    errorPlay();
                }
            }
        });

        npSleepTime.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                try{
                    if (sCheckSleep.isChecked()) prefs.edit().putInt("SLEEP_TIME", newVal).apply();
                    else prefs.edit().putInt("SLEEP_TIME", 0).apply();
                }catch(Exception ex){
                    errorPlay();
                }
            }
        });

        sTimeFormat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    prefs.edit().putBoolean("TIME_FORMAT", sTimeFormat.isChecked()).apply();
                }catch(Exception ex){
                    errorPlay();
                }
            }
        });

        return root;
    }

    private void loadPlay() {
        lCheckLoad.setAnimation(R.raw.check_mark_settings);
        lCheckLoad.playAnimation();
    }

    private void errorPlay(){
        lCheckLoad.setAnimation(R.raw.exclamation_mark);
        lCheckLoad.playAnimation();
    }

    private void init() {//TODO не сохраняются анимации и темы
        sAnim = binding.sAnimations;
        sCheckSleep = binding.sSleepTime;
        sTimeFormat = binding.sTimeFormat;
        npCardCount = binding.npCycles;
        npSleepTime = binding.npTimeSleep;
        npCycleDuration = binding.npDurationCycle;
        rgTheme = binding.rgTheme;
        lCheckLoad = binding.lCheckLoad;
        rbAuto = binding.rbThemeAuto;
        bSetDefault = binding.bSettingsDefault;
        rlSettings = binding.rlSettingsBg;

        prefs = getContext().getSharedPreferences("SETTINGS", Context.MODE_PRIVATE);

        rgTheme.check(prefs.getInt("THEME", R.id.rbThemeAuto));
        sAnim.setChecked(prefs.getBoolean("ANIMATIONS", true));
        npCardCount.setValue(prefs.getInt("CARD_COUNT", 6));
        sCheckSleep.setChecked(prefs.getBoolean("CHECK_SLEEP", false));
        npSleepTime.setEnabled(sCheckSleep.isChecked());
        npSleepTime.setValue(prefs.getInt("SLEEP_TIME", 0));
        sTimeFormat.setChecked(prefs.getBoolean("TIME_FORMAT", true));
        npCycleDuration.setValue(prefs.getInt("CYCLE_DURATION", 90));
    }

    private void ClearSharedPreferences(){
        prefs.edit().clear().apply();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}