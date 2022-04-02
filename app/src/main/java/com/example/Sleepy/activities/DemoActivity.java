package com.example.Sleepy.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
    private LinearLayout llOnboarding;
    private Button bNext, bSkip;
    ArrayList<OnBoarding> states = new ArrayList<OnBoarding>();
    OnBoardingAdapter boardAdapter = new OnBoardingAdapter(states);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppTheme.setShareTheme(getApplicationContext());
        setContentView(R.layout.activity_demo);
        binding = ActivityDemoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        final ViewPager2 vpCards = binding.vpDemo;
        vpCards.setAdapter(boardAdapter);
        llOnboarding = binding.llIndicators;
        bNext = binding.bNext;
        bSkip = binding.bSkip;

        initCardItem();
        setCurrentIndicator(0);
        setOnboardingIndicators();
        vpCards.setCurrentItem(vpCards.getCurrentItem());

        vpCards.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentIndicator(position);
            }
        });

        bNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyVibrator.vibrate(30, DemoActivity.this);
                if(vpCards.getCurrentItem() + 1 < boardAdapter.getItemCount()){
                    vpCards.setCurrentItem(vpCards.getCurrentItem() + 1);
                }else {
                    bNext.setEnabled(false);
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                }
            }
        });

        bSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bSkip.setEnabled(false);
                MyVibrator.vibrate(30, DemoActivity.this);
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });
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

    private void setOnboardingIndicators(){
        ImageView[] indicators = new ImageView[boardAdapter.getItemCount()];
        LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        lParams.setMargins(8,0,8,0);
        for(int i = 0; i < indicators.length; i++){
            indicators[i] = new ImageView(getApplicationContext());
            indicators[i].setImageDrawable(ContextCompat.getDrawable(
                    getApplicationContext(),
                    R.drawable.board_ind_active//active
            ));
            indicators[i].setLayoutParams(lParams);
            llOnboarding.addView(indicators[i]);
        }
    }

    private void setCurrentIndicator(int ind){
        int childCount = llOnboarding.getChildCount();
        for(int i = 0; i < childCount; i++){
            ImageView iv = (ImageView) llOnboarding.getChildAt(i);
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
            bNext.setText(R.string.board_start_activity);
        }else {
            bNext.setText(R.string.board_next);
        }
    }

}