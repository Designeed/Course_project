package com.example.Sleepy.ui.settings;

import android.content.SharedPreferences;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


public class SettingsViewModel extends ViewModel{

    private MutableLiveData<Boolean> AutoTheme;
    private MutableLiveData<Boolean> ShowAnimations;
    private MutableLiveData<Boolean> CheckSleep;
    private MutableLiveData<Boolean> TimeFormat;

    private MutableLiveData<Integer> Theme;
    private MutableLiveData<Integer> CardCount;
    private MutableLiveData<Integer> TimeSleep;
    private MutableLiveData<Integer> SleepCycle;

    private SharedPreferences prefs;

    public MutableLiveData<Integer> getTheme() {
        return Theme;
    }

    public void setTheme(MutableLiveData<Integer> theme) {
        Theme = theme;
    }

    public MutableLiveData<Boolean> getAutoTheme() {
        return AutoTheme;
    }

    public void setAutoTheme(MutableLiveData<Boolean> autoTheme) {
        AutoTheme = autoTheme;
    }

    public MutableLiveData<Boolean> getShowAnimations() {
        return ShowAnimations;
    }

    public void setShowAnimations(MutableLiveData<Boolean> showAnimations) {
        ShowAnimations = showAnimations;
    }

    public MutableLiveData<Boolean> getCheckSleep() {
        return CheckSleep;
    }

    public void setCheckSleep(MutableLiveData<Boolean> checkSleep) {
        CheckSleep = checkSleep;
    }

    public MutableLiveData<Boolean> getTimeFormat() {
        return TimeFormat;
    }

    public void setTimeFormat(MutableLiveData<Boolean> timeFormat) {
        TimeFormat = timeFormat;
    }

    public MutableLiveData<Integer> getCardCount() {
        return CardCount;
    }

    public void setCardCount(MutableLiveData<Integer> cardCount) {
        CardCount = cardCount;
    }

    public MutableLiveData<Integer> getTimeSleep() {
        return TimeSleep;
    }

    public void setTimeSleep(MutableLiveData<Integer> timeSleep) {
        TimeSleep = timeSleep;
    }

    public MutableLiveData<Integer> getSleepCycle() {
        return SleepCycle;
    }

    public void setSleepCycle(MutableLiveData<Integer> sleepCycle) {
        SleepCycle = sleepCycle;
    }
}