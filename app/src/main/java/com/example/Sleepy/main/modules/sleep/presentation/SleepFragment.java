package com.example.Sleepy.main.modules.sleep.presentation;

import static com.example.Sleepy.shared.MyTimer.getFormatTime;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.Sleepy.R;
import com.example.Sleepy.core.presentation.MainActivity;
import com.example.Sleepy.main.modules.sleep.domain.TimeCards;
import com.example.Sleepy.main.modules.sleep.domain.TimeCardsAdapter;
import com.example.Sleepy.shared.MyAlarm;
import com.example.Sleepy.shared.MyAnimator;
import com.example.Sleepy.shared.MyPreferences;
import com.example.Sleepy.shared.MyTimer;
import com.example.Sleepy.shared.MyVibrator;
import com.example.Sleepy.shared.Quotes;
import com.example.Sleepy.databinding.FragmentSleepBinding;
import com.example.Sleepy.main.modules.sleep.domain.SleepViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
        sleepViewModel = new SleepViewModel(Objects.requireNonNull(getContext()));
        binding = FragmentSleepBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        init();
        initCardItem();
        MyAnimator.setFadeAnimation(root);

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

            if(q.equals(getString(R.string.you_need_anecdote))){
                s.setAction(R.string.yes, view1 -> new MaterialAlertDialogBuilder(Objects.requireNonNull(getContext()))
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

        MyTimer.getAsleepText(fallingAsleepTime, binding.tvTimeAsleep, getContext());
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
        MyPreferences.SettingsApp prefs = new MyPreferences.SettingsApp(requireContext());
        cardCount = prefs.getCardCount();
        fallingAsleepTime = prefs.getSleepTime();
        cycleDuration = prefs.getCycleDuration();
        isAnimate = prefs.isAnimated();
    }

    private void initCardItem() {
        binding.rvCards.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvCards.setAdapter(timeCardsAdapter);

        timeCards.clear();

        curTimeFull.add(Calendar.MINUTE, Minutes * (cardCount + 1));
        curTimeFull.add(Calendar.MINUTE, fallingAsleepTime);
        remMinutes = remMinutes * cardCount;

        for(int i = 0; i < cardCount; i++){
            curTimeFull.add(Calendar.MINUTE, -Minutes);
            timeCards.add(new TimeCards((sdf.format(curTimeFull.getTime())), (getString(R.string.remaining_time) + getFormatTime(remMinutes, getContext()))));
            remMinutes -= cycleDuration;
        }

        Log.i("initCard", "initCard");
    }

    private void setTitleTime(){
        if (timeCards.size() >= 6) mainAct.setTitleAppBar(getString(R.string.optimal_time) + timeCards.get(timeCards.size() - 6).getTitle());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        mainAct.setTitleAppBar(null);
        timeCards.clear();
    }
}