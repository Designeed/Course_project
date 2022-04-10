package com.example.Sleepy.classes;

import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Interpolator;

public class MyAnimator {
    public static void setFadeAnimation(View view)
    {
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(500);
        anim.setInterpolator(new AccelerateInterpolator());
        view.startAnimation(anim);
    }
}
