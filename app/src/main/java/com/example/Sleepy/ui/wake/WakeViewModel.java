package com.example.Sleepy.ui.wake;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class WakeViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public WakeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Когда проснуться");
    }

    public LiveData<String> getText() {
        return mText;
    }
}