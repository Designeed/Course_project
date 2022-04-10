package com.example.Sleepy.activities;

import static android.media.AudioManager.STREAM_ALARM;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.Sleepy.adapters.OnBoarding;
import com.example.Sleepy.R;
import com.example.Sleepy.adapters.OnBoardingAdapter;
import com.example.Sleepy.classes.AppTheme;
import com.example.Sleepy.classes.MyVibrator;
import com.example.Sleepy.databinding.ActivityDemoBinding;

import java.util.ArrayList;

public class DemoActivity extends AppCompatActivity {

    private ActivityDemoBinding binding;
    ArrayList<OnBoarding> states;
    OnBoardingAdapter boardAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppTheme.setShareTheme(getApplicationContext());
        setContentView(R.layout.activity_demo);
        binding = ActivityDemoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
        initCardItem();
        setCurrentIndicator(0);
        setOnBoardingIndicators();
        binding.vpDemo.setCurrentItem(binding.vpDemo.getCurrentItem());

        binding.vpDemo.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentIndicator(position);
            }
        });

        binding.bNext.setOnClickListener(view -> {
            MyVibrator.vibrate(30, DemoActivity.this);
            if(binding.vpDemo.getCurrentItem() + 1 < boardAdapter.getItemCount()){
                binding.vpDemo.setCurrentItem(binding.vpDemo.getCurrentItem() + 1);
            }else {
                binding.bNext.setEnabled(false);
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                finish();
            }
        });

        binding.bSkip.setOnClickListener(view -> {
            binding.bSkip.setEnabled(false);
            MyVibrator.vibrate(30, DemoActivity.this);
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        });
    }

    private void init(){
        setVolumeControlStream(STREAM_ALARM);
        states = new ArrayList<>();
        boardAdapter = new OnBoardingAdapter(states);
        binding.vpDemo.setAdapter(boardAdapter);
    }

    private void initCardItem() {
        OnBoarding item1 = new OnBoarding();
        item1.setTitle(getString(R.string.app_name));
        item1.setDescription(getString(R.string.item1_description));
        item1.setImage(R.drawable.board_1_svg);

        OnBoarding item2 = new OnBoarding();
        item2.setTitle(getString(R.string.app_name));
        item2.setDescription(getString(R.string.item2_description));
        item2.setImage(R.drawable.board_2_svg);

        OnBoarding item3 = new OnBoarding();
        item3.setTitle(getString(R.string.app_name));
        item3.setDescription(getString(R.string.item3_description));
        item3.setImage(R.drawable.board_3_svg);

        states.add(item1);
        states.add(item2);
        states.add(item3);

        boardAdapter = new OnBoardingAdapter(states);
    }

    private void setOnBoardingIndicators(){
        ImageView[] indicators = new ImageView[boardAdapter.getItemCount()];
        LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        lParams.setMargins(8,0,8,0);
        for(int i = 0; i < indicators.length; i++){
            indicators[i] = new ImageView(getApplicationContext());
            indicators[i].setImageDrawable(ContextCompat.getDrawable(
                    getApplicationContext(),
                    R.drawable.board_ind_active//active
            ));
            indicators[i].setLayoutParams(lParams);
            binding.llIndicators.addView(indicators[i]);
        }
    }

    private void setCurrentIndicator(int ind){
        int childCount =  binding.llIndicators.getChildCount();
        for(int i = 0; i < childCount; i++){
            ImageView iv = (ImageView)  binding.llIndicators.getChildAt(i);
            if(i == ind){
                iv.setImageDrawable(
                        ContextCompat.getDrawable(
                                getApplicationContext(),
                                R.drawable.board_ind_active//active
                        )
                );
            } else{
                iv.setImageDrawable(
                        ContextCompat.getDrawable(
                                getApplicationContext(),
                                R.drawable.board_ind_inactive//inactive
                        )
                );
            }
        }
        if(ind == boardAdapter.getItemCount() - 1){
            binding.bNext.setText(R.string.board_start_activity);
        }else {
            binding.bNext.setText(R.string.board_next);
        }
    }

}