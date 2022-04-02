package com.example.Sleepy.adapters;

public class AlarmCards {
    private String Time;
    private String RemainingTime;

    public AlarmCards(String formatTime, String RemainingTimeMinute) {
        Time = formatTime;
        RemainingTime = RemainingTimeMinute;
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
}
