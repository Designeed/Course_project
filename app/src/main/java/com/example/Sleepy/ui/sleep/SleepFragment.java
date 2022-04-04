package com.example.Sleepy.ui.sleep;

import static com.example.Sleepy.classes.MyTimer.getFormatTime;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.Sleepy.activities.MainActivity;
import com.example.Sleepy.adapters.TimeCards;
import com.example.Sleepy.adapters.TimeCardsAdapter;
import com.example.Sleepy.classes.MyAlarm;
import com.example.Sleepy.classes.MyVibrator;
import com.example.Sleepy.databinding.FragmentSleepBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Objects;

public class SleepFragment extends Fragment {
    private SleepViewModel sleepViewModel;
    private FragmentSleepBinding binding;
    final private Calendar curTimeFull = Calendar.getInstance();
    private final ArrayList<TimeCards> timeCards = new ArrayList<>();
    private final TimeCardsAdapter timeCardsAdapter = new TimeCardsAdapter(timeCards);
    private SimpleDateFormat sdf;
    private int cardCount = 6, Min = -90, remMin = 90, cycleDuration, fallingAsleepTime;
    private MainActivity mainAct;
    private boolean isAnimate;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        sleepViewModel = new ViewModelProvider(this).get(SleepViewModel.class);
        binding = FragmentSleepBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        init();
        initCardItem();

        sleepViewModel.getText().observe(getViewLifecycleOwner(), s -> binding.tvSetTime.setText(s));

        sleepViewModel.getCurTime().observe(getViewLifecycleOwner(), date -> {
            binding.tpSleep.setHour(date.get(Calendar.HOUR_OF_DAY));
            binding.tpSleep.setMinute(date.get(Calendar.MINUTE));
            binding.tpSleep.setIs24HourView(true);
        });

        sleepViewModel.getTextSleep().observe(getViewLifecycleOwner(), s -> binding.tvGoToSleep.setText(s));

        binding.bCalc.setOnClickListener(v -> {
            MyVibrator.vibrate(30, getContext());
            remMin = cycleDuration;
            curTimeFull.set(Calendar.YEAR, Calendar.MONTH, Calendar.DATE, binding.tpSleep.getHour(), binding.tpSleep.getMinute());
            initCardItem();
            setTitleTime();
        });

        binding.tpSleep.setOnTimeChangedListener((view, hourOfDay, minute) -> {
            Calendar c = new GregorianCalendar();
            c.set(Calendar.getInstance().get(Calendar.YEAR),
                    Calendar.getInstance().get(Calendar.MONTH),
                    Calendar.getInstance().get(Calendar.DATE),
                    hourOfDay,
                    minute);
            sleepViewModel.setCurTime(c);
            MyVibrator.vibrate(15, getContext());
        });

        binding.bClearTime.setOnClickListener(view -> {
            MyVibrator.vibrate(30, getContext());
            binding.tpSleep.setHour(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
            binding.tpSleep.setMinute(Calendar.getInstance().get(Calendar.MINUTE));
        });

        binding.svMainSleep.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) ->
                binding.lStarTimePicker.setFrame(scrollY/4));

        binding.bAddAlarm.setOnClickListener(view -> {
            Calendar timeAlarm = Calendar.getInstance();
            timeAlarm.set(Calendar.MINUTE, binding.tpSleep.getMinute());
            timeAlarm.set(Calendar.HOUR_OF_DAY, binding.tpSleep.getHour());
            setAlarm(timeAlarm, binding.clMain);
        });

        return root;
    }

    private void init() {
        mainAct = (MainActivity) getActivity();
        curTimeFull.set(Calendar.YEAR, Calendar.MONTH, Calendar.DATE, binding.tpSleep.getHour(), binding.tpSleep.getMinute());
        sdf = new SimpleDateFormat("HH:mm");

        remMin = cycleDuration;
        Min = -cycleDuration;

        getShared();
        getAsleepText();
        getAnimations();
    }

    private void getAnimations() {
        if (!isAnimate){
            binding.lCatSleepMain.setSpeed(0);
        }else{
            binding.lCatSleepMain.setSpeed(1);
        }
    }

    private void getAsleepText() {
        if (fallingAsleepTime != 0){
            binding.tvTimeAsleep.setVisibility(View.VISIBLE);
            binding.tvTimeAsleep.setText(new StringBuilder().append("Расчитано с учетом времени засыпания - ").append(fallingAsleepTime).append(" мин."));
        }else {
            binding.tvTimeAsleep.setVisibility(View.GONE);
        }
    }

    private void getShared(){
        SharedPreferences prefs = Objects.requireNonNull(getContext()).getSharedPreferences("SETTINGS", Context.MODE_PRIVATE);
        cardCount = prefs.getInt("CARD_COUNT", 6);
        fallingAsleepTime = prefs.getInt("SLEEP_TIME", 0);
        cycleDuration = prefs.getInt("CYCLE_DURATION", 90) + fallingAsleepTime;
        isAnimate = prefs.getBoolean("ANIMATIONS", true);
    }

    private void setAlarm(Calendar time, View view){
        Log.i("alarm", "Time Alarm - " + time.get(Calendar.HOUR_OF_DAY) + ":" + time.get(Calendar.MINUTE));
        MyAlarm.setAlarm(time, view);
    }

    private void initCardItem() {
        binding.rvCards.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvCards.setAdapter(timeCardsAdapter);

        timeCards.clear();

        curTimeFull.add(Calendar.MINUTE, Min * (cardCount + 1));
        remMin = remMin * cardCount;

        for(int i = 0; i < cardCount; i++){
            curTimeFull.add(Calendar.MINUTE, -Min);
            timeCards.add(new TimeCards(("" + sdf.format(curTimeFull.getTime())), ("Осталось " + getFormatTime(remMin))));
            remMin -= cycleDuration;
        }
        Log.i("initCard", "initCard");
    }

    private void setTitleTime(){
        if (timeCards.size()>=6) mainAct.setTitleAppBar("Оптимальное время - " + timeCards.get(timeCards.size()-6).getTitle());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        mainAct.setTitleAppBar(null);
    }
}