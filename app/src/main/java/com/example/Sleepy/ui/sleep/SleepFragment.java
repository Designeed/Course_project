package com.example.Sleepy.ui.sleep;

import static com.example.Sleepy.classes.MyTimer.getFormatTime;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.Sleepy.activities.MainActivity;
import com.example.Sleepy.adapters.TimeCards;
import com.example.Sleepy.adapters.TimeCardsAdapter;
import com.example.Sleepy.classes.MyAlarm;
import com.example.Sleepy.classes.MyVibrator;
import com.example.Sleepy.databinding.FragmentSleepBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class SleepFragment extends Fragment {
    private SleepViewModel sleepViewModel;
    private FragmentSleepBinding binding;
    final private Calendar curTimeFull = new GregorianCalendar();
    private final ArrayList<TimeCards> timeCards = new ArrayList<TimeCards>();
    private final TimeCardsAdapter timeCardsAdapter = new TimeCardsAdapter(timeCards);
    private SimpleDateFormat sdf;
    private SharedPreferences prefs;
    private int cardCount = 6, Min = -90, remMin = 90, cycleDuration, fallingAsleepTime;
    private MainActivity mainAct;
    private TextView tvSetTime, tvGoToSleep, tvFallingAsleepTime;
    private TimePicker tpSetTime;
    private Button bCalc, bReload, bSetAlarm;
    private LottieAnimationView lTimePicker, lCat;
    private boolean isAnimate;
    private NestedScrollView svSleep;
    private CoordinatorLayout clMain;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        sleepViewModel = new ViewModelProvider(this).get(SleepViewModel.class);
        binding = FragmentSleepBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        init();
        initCardItem();

        sleepViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                tvSetTime.setText(s);
            }
        });

        sleepViewModel.getCurTime().observe(getViewLifecycleOwner(), new Observer<Calendar>() {
            @Override
            public void onChanged(Calendar date) {
                tpSetTime.setCurrentHour(date.getTime().getHours());
                tpSetTime.setCurrentMinute(date.getTime().getMinutes());
                tpSetTime.setIs24HourView(true);
            }
        });

        sleepViewModel.getTextSleep().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                tvGoToSleep.setText(s);
            }
        });

        bCalc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyVibrator.vibrate(30, getContext());
                remMin=cycleDuration;
                curTimeFull.set(Calendar.YEAR, Calendar.MONTH, Calendar.DATE, tpSetTime.getHour(), tpSetTime.getMinute());
                initCardItem();
                setTitleTime();
            }
        });

        tpSetTime.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                Calendar c = new GregorianCalendar();
                c.set(Calendar.getInstance().getTime().getYear(),
                        Calendar.getInstance().getTime().getMonth(),
                        Calendar.getInstance().getTime().getDay(),
                        hourOfDay,
                        minute);
                sleepViewModel.setCurTime(c);
                MyVibrator.vibrate(15, getContext());
            }
        });

        bReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyVibrator.vibrate(30, getContext());
                tpSetTime.setHour(new Date().getHours());
                tpSetTime.setMinute(new Date().getMinutes());
            }
        });

        svSleep.setSmoothScrollingEnabled(true);
        svSleep.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                lTimePicker.setFrame(scrollY/4);
            }
        });

        bSetAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar timeAlarm = Calendar.getInstance();
                timeAlarm.set(Calendar.MINUTE, tpSetTime.getMinute());
                timeAlarm.set(Calendar.HOUR_OF_DAY, tpSetTime.getHour());
                setAlarm(timeAlarm, clMain);
            }
        });

        return root;
    }

    private void init() {
        tvSetTime = binding.tvSetTime;
        tvGoToSleep = binding.tvGoToSleep;
        tpSetTime = binding.tpSleep;
        bCalc = binding.bCalc;
        bReload = binding.bClearTime;
        bSetAlarm = binding.bAddAlarm;
        lTimePicker = binding.lStarTimePicker;
        lCat = binding.lCatSleepMain;
        tvFallingAsleepTime = binding.tvTimeAsleep;
        svSleep = binding.svMainSleep;
        clMain = binding.clMain;
        mainAct = (MainActivity) getActivity();

        curTimeFull.set(Calendar.YEAR, Calendar.MONTH, Calendar.DATE, tpSetTime.getHour(), tpSetTime.getMinute());
        sdf = new SimpleDateFormat("HH:mm");
        prefs = getContext().getSharedPreferences("SETTINGS", Context.MODE_PRIVATE);
        cardCount = prefs.getInt("CARD_COUNT", 6);
        fallingAsleepTime = prefs.getInt("SLEEP_TIME", 0);
        cycleDuration = prefs.getInt("CYCLE_DURATION", 90) + fallingAsleepTime;
        remMin = cycleDuration;
        Min = -cycleDuration;
        isAnimate = prefs.getBoolean("ANIMATIONS", true);

        if (fallingAsleepTime != 0){
            tvFallingAsleepTime.setVisibility(View.VISIBLE);
            tvFallingAsleepTime.setText("Расчитано с учетом времени засыпания - " + fallingAsleepTime + " мин.");
        }else {
            tvFallingAsleepTime.setVisibility(View.GONE);
        }

        if (!isAnimate){
            lCat.setSpeed(0);
        }else{
            lCat.setSpeed(1);
        }
    }

    private void setAlarm(Calendar time, View view){
        Log.i("alarm", "Time Alarm - " + time.getTime().getHours() + ":" + time.getTime().getMinutes());
        MyAlarm.setAlarm(time, view);
    }

    private void initCardItem() {
        //TODO настройка кароче чтобы карточки прошедшего времени не отображались сделай пж
        //TODO темы для всех активити
        //TODO перенести
        final RecyclerView rvCards = binding.rvCards;
        rvCards.setLayoutManager(new LinearLayoutManager(getContext()));
        rvCards.setAdapter(timeCardsAdapter);
        Log.i("initCard", "initCard");
        timeCards.clear();

        curTimeFull.add(Calendar.MINUTE, Min * (cardCount + 1));
        remMin = remMin * cardCount;

        for(int i = 0; i < cardCount; i++){
            curTimeFull.add(Calendar.MINUTE, -Min);
            timeCards.add(new TimeCards(("" + sdf.format(curTimeFull.getTime())), ("Осталось " + getFormatTime(remMin))));
            remMin -= cycleDuration;
        }
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