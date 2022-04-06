package com.example.Sleepy.ui.wake;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Calendar;

public class WakeViewModel extends ViewModel {

    private MutableLiveData<String> SetTimeText;
    private MutableLiveData<String> SetWakeText;
    private MutableLiveData<Calendar> CurTime;

    public WakeViewModel() {
        SetTimeText = new MutableLiveData<>();
        SetTimeText.setValue("Выберите время, в которое хотели бы лечь спать...");

        SetWakeText = new MutableLiveData<>();
        SetWakeText.setValue("Вам нужно проснуться в...");

        CurTime = new MutableLiveData<>();
        CurTime.setValue(Calendar.getInstance());
    }

    public LiveData<String> getTextWake(){ return SetWakeText; }
    public LiveData<String> getText() {
        return SetTimeText;
    }
    public LiveData<Calendar> getCurTime(){ return CurTime; }

    public void setCurTime(Calendar calendar){
        CurTime.setValue(calendar);
    }
}