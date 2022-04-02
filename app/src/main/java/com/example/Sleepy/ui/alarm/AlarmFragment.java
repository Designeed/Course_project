package com.example.Sleepy.ui.alarm;

import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.Sleepy.R;
import com.example.Sleepy.adapters.AlarmCards;
import com.example.Sleepy.adapters.AlarmCardsAdapter;
import com.example.Sleepy.classes.MyAlarm;
import com.example.Sleepy.classes.MyTimer;
import com.example.Sleepy.databinding.FragmentAlarmBinding;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class AlarmFragment extends Fragment {

    private AlarmViewModel alarmViewModel;
    private FragmentAlarmBinding binding;
    private ExtendedFloatingActionButton fabAdd;
    private Calendar timeAlarm = Calendar.getInstance();
    private CoordinatorLayout clMain;
    private SharedPreferences prefs = null;
    private RecyclerView rvTimeList;
    private ArrayList<AlarmCards> alarmCards;
    private AlarmCardsAdapter alarmCardsAdapter;
    private AlarmManager alarmManager;
    private TextView tvAlarmHeader;
    private MaterialButton bCancelAlarm;
    private MaterialTimePicker mtpTimeAlarm;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        alarmViewModel = new ViewModelProvider(this).get(AlarmViewModel.class);

        binding = FragmentAlarmBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        init();
        initCardItem();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(getActivity())) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getActivity().getPackageName()));
                startActivity(intent);
            }
        }

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mtpTimeAlarm = new MaterialTimePicker.Builder()
                        .setTimeFormat(TimeFormat.CLOCK_24H)
                        .setHour(12)
                        .setMinute(0)
                        .setTitleText("Выберите время")
                        .build();

                mtpTimeAlarm.addOnPositiveButtonClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        timeAlarm.set(Calendar.MINUTE, mtpTimeAlarm.getMinute());
                        timeAlarm.set(Calendar.HOUR_OF_DAY, mtpTimeAlarm.getHour());
                        setAlarm(timeAlarm, clMain);
                    }
                });

                mtpTimeAlarm.show(getChildFragmentManager(), "tag_picker");
            }
        });

        bCancelAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar timeLastAlarm = new GregorianCalendar();
                timeLastAlarm.setTime(calcAlarmTime());
                MyAlarm.cancelAlarm(clMain);
                initCardItem();

                Snackbar.make(
                        clMain,
                        "Будильник на " + new SimpleDateFormat("HH:mm").format(timeLastAlarm.getTime()) + " отменен",
                        Snackbar.LENGTH_LONG)
                        .setAction("Вернуть", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setAlarm(timeLastAlarm, clMain);
                    }
                }).show();
            }
        });

        return root;
    }

    private void setAlarm(Calendar time, View view){
        Log.i("alarm", "Time Alarm - " + time.getTime().getHours() + ":" + time.getTime().getMinutes());
        MyAlarm.setAlarm(time, view);
        initCardItem();
    }

    private void initCardItem() {
        rvTimeList.setLayoutManager(new LinearLayoutManager(getContext()));
        rvTimeList.setAdapter(alarmCardsAdapter);

        if(calcAlarmTime() != null){
            bCancelAlarm.setVisibility(View.VISIBLE);
            tvAlarmHeader.setText(R.string.alarm_list);

            alarmCards.clear();
            alarmCards.add(new AlarmCards(
                    "" + new SimpleDateFormat("HH:mm").format(calcAlarmTime()),
                    "Осталось " + MyTimer.calcRemainingTimeMinute(calcAlarmTime())));
        }else{
            alarmCards.clear();
            tvAlarmHeader.setText("Пока что нет будильников 😔\n");
            bCancelAlarm.setVisibility(View.GONE);
        }
    }

    private Date calcAlarmTime(){
        if(alarmManager.getNextAlarmClock() != null){
            long time = alarmManager.getNextAlarmClock().getTriggerTime();
            Date timeInDate = new Date(time);
            Log.i("INIT_CARD", "GET_TIME " + time);
            return timeInDate;
        }else{
            return null;
        }
    }

    private void init(){
        fabAdd = binding.fabAddAlarm;
        clMain = binding.clAlarm;
        prefs = getContext().getSharedPreferences("ALARM_MANAGER", Context.MODE_PRIVATE);
        rvTimeList = binding.rvAlarmList;
        alarmCards = new ArrayList<>();
        alarmCardsAdapter = new AlarmCardsAdapter(alarmCards);
        alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        tvAlarmHeader = binding.tvAlarmList;
        bCancelAlarm = binding.bCancelAlarm;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}