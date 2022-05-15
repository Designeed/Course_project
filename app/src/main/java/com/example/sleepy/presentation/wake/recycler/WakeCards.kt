package com.example.sleepy.presentation.wake.recycler

import java.util.*

class WakeCards(var time: String, var remainingTime: String, triggerTime: Calendar) {
    private val triggerTime = Calendar.getInstance()

    fun setTriggerTime(triggerTime: Calendar) {
        this.triggerTime.time = triggerTime.time
    }

    fun getTriggerTime(): Calendar {
        return triggerTime
    }

    init {
        this.triggerTime[triggerTime[Calendar.YEAR], triggerTime[Calendar.MONTH], triggerTime[Calendar.DATE], triggerTime[Calendar.HOUR_OF_DAY]] =
            triggerTime[Calendar.MINUTE]
    }
}