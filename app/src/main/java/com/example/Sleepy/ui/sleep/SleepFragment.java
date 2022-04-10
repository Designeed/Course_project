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

import com.example.Sleepy.R;
import com.example.Sleepy.activities.MainActivity;
import com.example.Sleepy.adapters.TimeCards;
import com.example.Sleepy.adapters.TimeCardsAdapter;
import com.example.Sleepy.classes.MyAlarm;
import com.example.Sleepy.classes.MyTimer;
import com.example.Sleepy.classes.MyVibrator;
import com.example.Sleepy.classes.Quotes;
import com.example.Sleepy.databinding.FragmentSleepBinding;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Objects;

public class SleepFragment extends Fragment {
    private SleepViewModel sleepViewModel;
    private FragmentSleepBinding binding;
    final private Calendar curTimeFull = Calendar.getInstance();
    private ArrayList<TimeCards> timeCards;
    private TimeCardsAdapter timeCardsAdapter;
    private SimpleDateFormat sdf;
    private int cardCount = 6, Minutes = -90, remMinutes = 90, cycleDuration, fallingAsleepTime;
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
            remMinutes = cycleDuration;
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

        binding.bClearTime.setOnClickListener(view -> MyTimer.clearTime(binding.tpSleep, getContext()));

        if(isAnimate){
            binding.svMainSleep.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) ->
                    binding.lStarTimePicker.setFrame(scrollY/8));
        }

        binding.bAddAlarm.setOnClickListener(view -> MyAlarm.setAlarm(getContext(), MyTimer.getCurrentTime(binding.tpSleep), binding.clMain));

        binding.lCatSleepMain.setOnClickListener(view -> {
            String q = Quotes.getQuoteCat();
            Snackbar s = Snackbar.make(view, q, Snackbar.LENGTH_LONG)
                    .setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE);

            if(q.equals("Хочешь анекдот?")){
                s.setAction("Да", view1 -> new MaterialAlertDialogBuilder(Objects.requireNonNull(getContext()))
                        .setTitle(getString(R.string.title_alert_cat))
                        .setMessage(Quotes.getAnecdote())
                        .setPositiveButton(getString(R.string.pos_b_alert), (dialogInterface, i) -> dialogInterface.cancel())
                        .show());
            }

            s.show();
        });

        return root;
    }

    private void init() {
        timeCards = new ArrayList<>();
        timeCardsAdapter = new TimeCardsAdapter(timeCards);
        mainAct = (MainActivity) getActivity();
        curTimeFull.set(Calendar.YEAR, Calendar.MONTH, Calendar.DATE, binding.tpSleep.getHour(), binding.tpSleep.getMinute());
        sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());

        getShared();

        remMinutes = cycleDuration;
        Minutes = -cycleDuration;

        MyTimer.getAsleepText(fallingAsleepTime, binding.tvTimeAsleep);
        getAnimations();
    }

    private void getAnimations() {
        if (!isAnimate){
            binding.lCatSleepMain.setSpeed(0);
        }else{
            binding.lCatSleepMain.setSpeed(1);
        }
    }

    private void getShared(){
        SharedPreferences prefs = Objects.requireNonNull(getContext()).getSharedPreferences("SETTINGS", Context.MODE_PRIVATE);
        cardCount = prefs.getInt("CARD_COUNT", 6);
        fallingAsleepTime = prefs.getInt("SLEEP_TIME", 0);
        cycleDuration = prefs.getInt("CYCLE_DURATION", 90);
        isAnimate = prefs.getBoolean("ANIMATIONS", true);
    }

    private void initCardItem() {
        // FIXME: 08.04.2022 время засыпания с инкрементом
        binding.rvCards.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvCards.setAdapter(timeCardsAdapter);

        timeCards.clear();

        curTimeFull.add(Calendar.MINUTE, Minutes * (cardCount + 1));
        curTimeFull.add(Calendar.MINUTE, fallingAsleepTime);
        remMinutes = remMinutes * cardCount;

        for(int i = 0; i < cardCount; i++){
            curTimeFull.add(Calendar.MINUTE, -Minutes);
            timeCards.add(new TimeCards(("" + sdf.format(curTimeFull.getTime())), ("Осталось " + getFormatTime(remMinutes))));
            remMinutes -= cycleDuration;
        }

        Log.i("initCard", "initCard");
    }

    private void setTitleTime(){
        if (timeCards.size() >= 6) mainAct.setTitleAppBar("Оптимальное время - " + timeCards.get(timeCards.size() - 6).getTitle());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        mainAct.setTitleAppBar(null);
        timeCards.clear();
    }
}