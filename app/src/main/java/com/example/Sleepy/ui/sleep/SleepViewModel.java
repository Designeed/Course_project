package com.example.Sleepy.ui.sleep;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.Sleepy.R;

import java.util.Calendar;

public class SleepViewModel extends ViewModel {

    private final MutableLiveData<String> SetTimeText;
    private final MutableLiveData<String> SetSleepText;
    private final MutableLiveData<Calendar> CurTime;

    public SleepViewModel(Context context) {
        SetTimeText = new MutableLiveData<>();
        SetTimeText.setValue(context.getString(R.string.choose_time));

        SetSleepText = new MutableLiveData<>();
        SetSleepText.setValue(context.getString(R.string.go_to_bed));

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