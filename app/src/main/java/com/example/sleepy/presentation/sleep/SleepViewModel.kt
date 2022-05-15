package com.example.sleepy.presentation.sleep

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.LiveData
import com.example.sleepy.R
import java.util.*

class SleepViewModel(context: Context) : ViewModel() {
    private val timeText: MutableLiveData<String> = MutableLiveData()
    private val sleepText: MutableLiveData<String>
    private val currentTime: MutableLiveData<Calendar>
    val textSleep: LiveData<String>
        get() = sleepText
    val text: LiveData<String>
        get() = timeText

    fun getCurTime(): LiveData<Calendar> {
        return currentTime
    }

    fun setCurTime(calendar: Calendar) {
        currentTime.value = calendar
    }

    init {
        timeText.value = context.getString(R.string.choose_time)
        sleepText = MutableLiveData()
        sleepText.value = context.getString(R.string.go_to_bed)
        currentTime = MutableLiveData()
        currentTime.value = Calendar.getInstance()
    }
}