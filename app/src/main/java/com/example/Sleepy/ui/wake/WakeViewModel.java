package com.example.Sleepy.ui.wake;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.Sleepy.R;

import java.util.Calendar;

public class WakeViewModel extends ViewModel {

    private final MutableLiveData<String> SetTimeText;
    private final MutableLiveData<String> SetWakeText;
    private final MutableLiveData<Calendar> CurTime;

    public WakeViewModel(Context context) {
        SetTimeText = new MutableLiveData<>();
        SetTimeText.setValue(context.getString(R.string.choose_time_go_to_bed));

        SetWakeText = new MutableLiveData<>();
        SetWakeText.setValue(context.getString(R.string.wake_up));

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