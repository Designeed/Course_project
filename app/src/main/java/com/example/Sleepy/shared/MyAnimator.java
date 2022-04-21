package com.example.Sleepy.shared;

import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

public class MyAnimator {
    public static void setFadeAnimation(View view)
    {
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(500);
        anim.setInterpolator(new AccelerateInterpolator());
        view.startAnimation(anim);
    }

    public static void setScaleAnimation(View view){
        ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setDuration(1000);
        view.startAnimation(anim);
    }
}
