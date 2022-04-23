package com.example.Sleepy.main.modules.wake.domain;

import java.util.Calendar;
import java.util.Date;

public class WakeCards {
    private String Time;
    private String RemainingTime;
    private Calendar TriggerTime = Calendar.getInstance();

    public WakeCards(String formatTime, String remainingTimeMinute, Calendar triggerTime) {
        Time = formatTime;
        RemainingTime = remainingTimeMinute;
        TriggerTime.set(triggerTime.get(Calendar.YEAR), triggerTime.get(Calendar.MONTH), triggerTime.get(Calendar.DATE), triggerTime.get(Calendar.HOUR_OF_DAY), triggerTime.get(Calendar.MINUTE));
    }

    public String getRemainingTime() {
        return RemainingTime;
    }

    public void setRemainingTime(String remainingTime) {
        RemainingTime = remainingTime;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public void setTriggerTime(Calendar triggerTime) {
        TriggerTime.setTime(triggerTime.getTime());
    }

    public Calendar getTriggerTime() {
        return TriggerTime;
    }
}
