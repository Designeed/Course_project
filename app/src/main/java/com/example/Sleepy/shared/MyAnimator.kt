package com.example.Sleepy.shared

import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.AccelerateInterpolator
import android.view.animation.ScaleAnimation
import android.view.animation.Animation

class MyAnimator {
    companion object{
        fun setFadeAnimation(view: View) {
            val anim = AlphaAnimation(0.0f, 1.0f)
            anim.duration = 500
            anim.interpolator = AccelerateInterpolator()
            view.startAnimation(anim)
        }

        fun setScaleAnimation(view: View) {
            val anim = ScaleAnimation(
                0.0f,
                1.0f,
                0.0f,
                1.0f,
                Animation.RELATIVE_TO_SELF,
                0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f
            )
            anim.duration = 1000
            view.startAnimation(anim)
        }
    }
}