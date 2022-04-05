package com.example.Sleepy.classes;

import java.util.Date;

public class MyTimer {

    public static String calcRemainingTimeMinute(Date currentDate){
        String RemTime;
        long timeUp = currentDate.getTime();
        long diff = timeUp - new Date().getTime();

        long diffMinutes = diff / (60 * 1000) % 60;
        long diffHours = diff / (60 * 60 * 1000) % 24;

        if (diffMinutes == 0){
            RemTime = "" + diffHours + "ч";
        }else if(diffHours == 0){
            RemTime = "" + diffMinutes + "м";
        }else{
            RemTime = "" + diffHours + "ч" + diffMinutes + "м";
        }
        return RemTime;
    }

    public static String getFormatTime(long minute){
        String RemTime;
        long h, m;
        h = minute / 60;
        m = minute - h * 60;
        if (m == 0){
            RemTime = "" + h + "ч";
        }else if(h != 0 && m != 0){
            RemTime = "" + h + "ч" + m + "м";
        }else{
            RemTime = "" + m + "м";
        }
        return RemTime;
    }
}
