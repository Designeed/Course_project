package com.example.Sleepy.shared;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.Sleepy.R;

public class MyPreferences {

    public static class SettingsApp{
        private final SharedPreferences preferences;
        public SettingsApp(Context context) {
            preferences = context.getSharedPreferences("SETTINGS", Context.MODE_PRIVATE);
        }

        public void clearSettingsApp(){
            preferences.edit().clear().apply();
        }

        public int getThemeId() {
            return preferences.getInt("THEME", R.id.rbThemeAuto);
        }

        public void setThemeId(int themeId) {
            preferences.edit().putInt("THEME", themeId).apply();
        }

        public boolean isAnimated() {
            return preferences.getBoolean("ANIMATIONS", true);
        }

        public void setAnimated(boolean isAnimated) {
            preferences.edit().putBoolean("THEME", isAnimated).apply();
        }

        public int getCardCount() {
            return preferences.getInt("CARD_COUNT", 6);
        }

        public void setCardCount(int cardCount){
            preferences.edit().putInt("CARD_COUNT", cardCount).apply();
        }

        public boolean isCheckedSleepTime(){
            return preferences.getBoolean("CHECK_SLEEP", false);
        }

        public void setCheckedSleepTime(boolean check){
            preferences.edit().putBoolean("CHECK_SLEEP", check).apply();
        }

        public int getSleepTime(){
            return preferences.getInt("SLEEP_TIME", 0);
        }

        public void setSleepTime(int time){
            preferences.edit().putInt("SLEEP_TIME", time).apply();
        }

        public boolean is24TimeFormat(){
            return preferences.getBoolean("TIME_FORMAT", true);
        }

        public void set24TimeFormat(boolean format){
            preferences.edit().putBoolean("TIME_FORMAT", format).apply();
        }

        public int getCycleDuration(){
            return preferences.getInt("CYCLE_DURATION", 90);
        }

        public void setCycleDuration(int duration){
            preferences.edit().putInt("CYCLE_DURATION", duration).apply();
        }

        public boolean isCheckedQuotes(){
            return preferences.getBoolean("QUOTE", true);
        }

        public void setCheckedQuotes(boolean check){
            preferences.edit().putBoolean("QUOTE", check).apply();
        }

        public boolean isBuiltinAlarm(){
            return preferences.getBoolean("WHAT_ALARM", true);
        }

        public void setCheckedBuiltinAlarm(boolean check){
            preferences.edit().putBoolean("WHAT_ALARM", check).apply();
        }

        public boolean isVibrated(){
            return preferences.getBoolean("VIBRATIONS", true);
        }

        public void setVibrated(boolean check){
            preferences.edit().putBoolean("VIBRATIONS", check).apply();
        }
    }

    public static class RunApp{
        private final SharedPreferences preferences;
        public RunApp(Context context){
            preferences = context.getSharedPreferences("SETTINGS_RUN", Context.MODE_PRIVATE);
        }

        public void clearRunApp(){
            preferences.edit().clear().apply();
        }

        public boolean isFirstRun(){
            return preferences.getBoolean("FIRST_RUN", true);
        }

        public void setCheckedRun(boolean checkedRun){
            preferences.edit().putBoolean("FIRST_RUN", checkedRun).apply();
        }
    }
}
