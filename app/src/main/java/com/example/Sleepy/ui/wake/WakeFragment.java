package com.example.Sleepy.ui.wake;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.Sleepy.R;
import com.example.Sleepy.activities.MainActivity;
import com.example.Sleepy.adapters.TimeCards;
import com.example.Sleepy.adapters.TimeCardsAdapter;
import com.example.Sleepy.classes.MyAlarm;
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
    final private Calendar curTimeFull = Calendar.getInstance();
    private final ArrayList<TimeCards> timeCards = new ArrayList<>();
    private final TimeCardsAdapter timeCardsAdapter = new TimeCardsAdapter(timeCards);
    private SimpleDateFormat sdf;
    private int cardCount = 6, Min = -90, remMin = 90, cycleDuration, fallingAsleepTime;
    private MainActivity mainAct;
    private boolean isAnimate, alarmType;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        wakeViewModel = new ViewModelProvider(this).get(WakeViewModel.class);
        binding = FragmentWakeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

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

        binding.svMainWake.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) ->
                binding.lStarTimePicker.setFrame(scrollY/4));

        binding.lYogaWake.setOnClickListener(view -> {
            String q = Quotes.getQuoteCat();
            Snackbar s = Snackbar.make(view, q, Snackbar.LENGTH_LONG)
                    .setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE);
            //todo написать другие реплики
            if(q.equals("Хочешь анекдот?")){
                s.setAction("Да", view1 -> new MaterialAlertDialogBuilder(Objects.requireNonNull(getContext()))
                        .setTitle(getString(R.string.title_alert_cat))
                        .setMessage(Quotes.getAnecdote())
                        .setPositiveButton(getString(R.string.pos_b_alert_cat), (dialogInterface, i) -> dialogInterface.cancel())
                        .show());
            }

            s.show();
        });

        return root;
    }

    private void init() {

    }

@Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}