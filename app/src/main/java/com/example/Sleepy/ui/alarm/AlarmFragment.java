package com.example.Sleepy.ui.alarm;

import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.Sleepy.R;
import com.example.Sleepy.adapters.TimeCards;
import com.example.Sleepy.adapters.TimeCardsAdapter;
import com.example.Sleepy.classes.MyAlarm;
import com.example.Sleepy.classes.MyTimer;
import com.example.Sleepy.databinding.FragmentAlarmBinding;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Objects;

public class AlarmFragment extends Fragment {

    private AlarmViewModel alarmViewModel;
    private FragmentAlarmBinding binding;
    private final Calendar timeAlarm = Calendar.getInstance();
    private ArrayList<TimeCards> alarmCards;
    private TimeCardsAdapter alarmCardsAdapter;
    private AlarmManager alarmManager;
    private MaterialTimePicker mtpTimeAlarm;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        alarmViewModel = new ViewModelProvider(this).get(AlarmViewModel.class);

        binding = FragmentAlarmBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        init();
        initCardItem();
        getPermission();

        binding.fabAddAlarm.setOnClickListener(view -> {
            mtpTimeAlarm = new MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_24H)
                    .setHour(12)
                    .setMinute(0)
                    .setTitleText("Выберите время")
                    .build();

            mtpTimeAlarm.addOnPositiveButtonClickListener(view1 -> {
                timeAlarm.set(Calendar.MINUTE, mtpTimeAlarm.getMinute());
                timeAlarm.set(Calendar.HOUR_OF_DAY, mtpTimeAlarm.getHour());
                setAlarm(timeAlarm, binding.clAlarm);
            });

            mtpTimeAlarm.show(getChildFragmentManager(), "tag_picker");
        });

        binding.bCancelAlarm.setOnClickListener(view -> {
            try{
                Calendar timeLastAlarm = new GregorianCalendar();
                if(calcAlarmTime() != null){
                    timeLastAlarm.setTime(Objects.requireNonNull(calcAlarmTime()));
                }
                MyAlarm.cancelAlarm(binding.clAlarm);
                initCardItem();

                Snackbar.make(
                        binding.clAlarm,
                        new StringBuilder()
                                .append("Будильник на ")
                                .append(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(timeLastAlarm.getTime()))
                                .append(" отменен"),
                        Snackbar.LENGTH_LONG)
                        .setAction("Вернуть", view12 -> setAlarm(timeLastAlarm, binding.clAlarm)).show();
            }catch (Exception ex){
                initCardItem();
                Snackbar.make(
                        binding.clAlarm,
                        "Будильник отменен",
                        Snackbar.LENGTH_LONG).show();
            }
        });

        return root;
    }

    private void init(){
        alarmCards = new ArrayList<>();
        alarmCardsAdapter = new TimeCardsAdapter(alarmCards);
        alarmManager = (AlarmManager) Objects.requireNonNull(getContext()).getSystemService(Context.ALARM_SERVICE);
    }

    private void getPermission() {
        if (!Settings.canDrawOverlays(getActivity())) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + Objects.requireNonNull(getActivity()).getPackageName()));
            startActivity(intent);
        }
    }

    private void setAlarm(Calendar time, View view){
        MyAlarm.setAlarm(getContext(), time, view);
        initCardItem();
        Log.i("alarm", "Time Alarm - " +
                time.get(Calendar.HOUR_OF_DAY) +
                ":" +
                time.get(Calendar.MINUTE));
    }

    private void initCardItem() {
        binding.rvAlarmList.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvAlarmList.setAdapter(alarmCardsAdapter);

        if(calcAlarmTime() != null){
            binding.bCancelAlarm.setVisibility(View.VISIBLE);
            binding.tvAlarmList.setText(R.string.alarm_list);

            alarmCards.clear();
            alarmCards.add(new TimeCards(
                    "" + new SimpleDateFormat("HH:mm", Locale.getDefault()).format(Objects.requireNonNull(calcAlarmTime())),
                    "Осталось " + MyTimer.calcRemainingTimeMinute(Objects.requireNonNull(calcAlarmTime()))));
        }else{
            alarmCards.clear();
            binding.tvAlarmList.setText("Пока что нет будильников 😔\n");
            binding.bCancelAlarm.setVisibility(View.GONE);
        }
    }

    private Date calcAlarmTime(){
        if(alarmManager.getNextAlarmClock() != null){
            long time = alarmManager.getNextAlarmClock().getTriggerTime();
            Date timeInDate = new Date(time);
            Log.i("INIT_CARD", "GET_TIME " + time);
            return timeInDate;
        }else {
            return null;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}