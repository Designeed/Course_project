package com.example.Sleepy.main.modules.wake.domain

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.LiveData
import com.example.Sleepy.R
import java.util.*

class WakeViewModel(context: Context) : ViewModel() {
    private val timeText: MutableLiveData<String> = MutableLiveData()
    private val wakeText: MutableLiveData<String>
    private val currentTime: MutableLiveData<Calendar>
    val textWake: LiveData<String>
        get() = wakeText
    val text: LiveData<String>
        get() = timeText

    fun getCurTime(): LiveData<Calendar> {
        return currentTime
    }

    fun setCurTime(calendar: Calendar) {
        currentTime.value = calendar
    }

    init {
        timeText.value = context.getString(R.string.choose_time_go_to_bed)
        wakeText = MutableLiveData()
        wakeText.value = context.getString(R.string.wake_up)
        currentTime = MutableLiveData()
        currentTime.value = Calendar.getInstance()
    }
}