package com.example.Sleepy.ui.sleep;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Calendar;

public class SleepViewModel extends ViewModel {

    private MutableLiveData<String> SetTimeText;
    private MutableLiveData<String> SetSleepText;
    private MutableLiveData<Calendar> CurTime;

    public SleepViewModel() {
        SetTimeText = new MutableLiveData<>();
        SetTimeText.setValue("Выберите время, в которое хотели бы проснуться...");

        SetSleepText = new MutableLiveData<>();
        SetSleepText.setValue("Ложитесь в...");

        CurTime = new MutableLiveData<>();
        CurTime.setValue(Calendar.getInstance());
    }

    public LiveData<String> getTextSleep(){ return SetSleepText; }
    public LiveData<String> getText() {
        return SetTimeText;
    }
    public LiveData<Calendar> getCurTime(){ return CurTime; }

    public void setCurTime(Calendar calendar){
        CurTime.setValue(calendar);
    }
}