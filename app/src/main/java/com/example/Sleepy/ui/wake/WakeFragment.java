package com.example.Sleepy.ui.wake;

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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.Sleepy.R;
import com.example.Sleepy.activities.MainActivity;
import com.example.Sleepy.adapters.WakeCards;
import com.example.Sleepy.adapters.WakeCardsAdapter;
import com.example.Sleepy.classes.MyAnimator;
import com.example.Sleepy.classes.MyTimer;
import com.example.Sleepy.classes.MyVibrator;
import com.example.Sleepy.classes.Quotes;
import com.example.Sleepy.databinding.FragmentWakeBinding;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Objects;

public class WakeFragment extends Fragment {

    private WakeViewModel wakeViewModel;
    private FragmentWakeBinding binding;
    final private Calendar curTime = Calendar.getInstance();
    private ArrayList<WakeCards> timeCards;
    private WakeCardsAdapter timeCardsAdapter;
    private SimpleDateFormat sdf;
    private int cardCount = 6, Minutes = -90, remMinutes = 90, cycleDuration, fallingAsleepTime;
    private MainActivity mainAct;
    private boolean isAnimate;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        wakeViewModel = new WakeViewModel(Objects.requireNonNull(getContext()));
        binding = FragmentWakeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        init();
        initCardItem();
        MyAnimator.setFadeAnimation(root);

        wakeViewModel.getText().observe(getViewLifecycleOwner(), s -> binding.tvSetTime.setText(s));

        wakeViewModel.getCurTime().observe(getViewLifecycleOwner(), date -> {
            binding.tpWake.setHour(date.get(Calendar.HOUR_OF_DAY));
            binding.tpWake.setMinute(date.get(Calendar.MINUTE));
            binding.tpWake.setIs24HourView(true);
        });

        wakeViewModel.getTextWake().observe(getViewLifecycleOwner(), s -> binding.tvGoToSleep.setText(s));

        binding.bClearTime.setOnClickListener(view -> MyTimer.clearTime(binding.tpWake, getContext()));

        binding.tpWake.setOnTimeChangedListener((view, hourOfDay, minute) -> {
            Calendar c = new GregorianCalendar();
            c.set(Calendar.getInstance().get(Calendar.YEAR),
                    Calendar.getInstance().get(Calendar.MONTH),
                    Calendar.getInstance().get(Calendar.DATE),
                    hourOfDay,
                    minute);
            wakeViewModel.setCurTime(c);
            MyVibrator.vibrate(15, getContext());
        });

        if(isAnimate){
            binding.svMainWake.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) ->
                    binding.lStarTimePicker.setFrame(scrollY/8));
        }

        binding.lYogaWake.setOnClickListener(view -> {
            String q = Quotes.getQuoteSloth();
            Snackbar s = Snackbar.make(view, q, Snackbar.LENGTH_LONG)
                    .setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE);

            if(q.equals(getString(R.string.you_need_fact))){
                s.setAction(R.string.yes, view1 -> new MaterialAlertDialogBuilder(Objects.requireNonNull(getContext()))
                        .setTitle(getString(R.string.title_alert_sloth))
                        .setMessage(Quotes.getFact())
                        .setPositiveButton(getString(R.string.pos_b_alert), (dialogInterface, i) -> dialogInterface.cancel())
                        .show());
            }

            s.show();
        });

        binding.bCalc.setOnClickListener(view ->{
            MyVibrator.vibrate(30, getContext());
            remMinutes = cycleDuration;
            setCurTime();
            initCardItem();
            setTitleTime();
        });

        return root;
    }

    private void init() {
        mainAct = (MainActivity) getActivity();
        timeCards = new ArrayList<>();
        timeCardsAdapter = new WakeCardsAdapter(timeCards);
        sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());

        getShared();
        setCurTime();

        remMinutes = cycleDuration;
        Minutes = cycleDuration;

        MyTimer.getAsleepText(fallingAsleepTime, binding.tvTimeAsleep, getContext());
        getAnimations();
    }

    private void setCurTime() {
        curTime.set(Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DATE), binding.tpWake.getHour(), binding.tpWake.getMinute());
        curTime.add(Calendar.MINUTE, fallingAsleepTime);
    }

    private void getAnimations() {
        if (!isAnimate){
            binding.lYogaWake.setSpeed(0);
        }else{
            binding.lYogaWake.setSpeed(1);
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
        binding.rvCards.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvCards.setAdapter(timeCardsAdapter);

        timeCards.clear();

        for(int i = 0; i < cardCount; i++){
            curTime.add(Calendar.MINUTE, Minutes);
            timeCards.add(new WakeCards((sdf.format(curTime.getTime())), (getString(R.string.time_to_sleep) + getFormatTime(remMinutes, getContext())), curTime));
            remMinutes += cycleDuration;
        }

        Log.i("initCard", "initCard_wake");
    }

    private void setTitleTime(){
        if (timeCards.size() >= 6) mainAct.setTitleAppBar(R.string.optimal_time + timeCards.get(6-1).getTime());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        mainAct.setTitleAppBar(null);
        timeCards.clear();
    }
}