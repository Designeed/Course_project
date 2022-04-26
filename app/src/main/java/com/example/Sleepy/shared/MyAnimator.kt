package com.example.Sleepy.shared

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.view.View
import android.view.animation.*

class MyAnimator {
    companion object{
        fun setFadeAnimationStart(view: View) {
            val anim = AlphaAnimation(0.0f, 1.0f)
            anim.duration = 500
            anim.interpolator = AccelerateInterpolator()
            view.startAnimation(anim)
        }

        fun setFadeAnimationEnd(view: View){
            val anim = AlphaAnimation(1.0f, 0.0f)
            anim.duration = 500
            anim.interpolator = AccelerateInterpolator()
            view.startAnimation(anim)
        }

        fun setScaleHeightObjectAnimation(view: View){
            val scaleX = PropertyValuesHolder.ofFloat(View.SCALE_X, 0f)
            val scaleY = PropertyValuesHolder.ofFloat(View.SCALE_Y, 4f)
            val animator = ObjectAnimator.ofPropertyValuesHolder(
                view, scaleX, scaleY)
            animator.start()
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