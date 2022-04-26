package com.example.Sleepy.shared

import android.view.MotionEvent

import android.util.DisplayMetrics

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.View
import kotlin.math.abs
import android.view.GestureDetector.SimpleOnGestureListener

import android.view.GestureDetector

import android.view.View.OnTouchListener
import java.lang.Exception


class MySwipeListener(ctx: Context?) : OnTouchListener {
    private val gestureDetector: GestureDetector

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        return gestureDetector.onTouchEvent(event)
    }

    private inner class GestureListener : SimpleOnGestureListener() {
        override fun onDown(e: MotionEvent): Boolean {
            return true
        }

        override fun onFling(
            e1: MotionEvent,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            var result = false
            try {
                val diffY = e2.y - e1.y
                val diffX = e2.x - e1.x
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > Companion.SWIPE_THRESHOLD && Math.abs(velocityX) > Companion.SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            onSwipeRight()
                        } else {
                            onSwipeLeft()
                        }
                        result = true
                    }
                } else if (Math.abs(diffY) > Companion.SWIPE_THRESHOLD && Math.abs(velocityY) > Companion.SWIPE_VELOCITY_THRESHOLD) {
                    if (diffY > 0) {
                        onSwipeBottom()
                    } else {
                        onSwipeTop()
                    }
                    result = true
                }
            } catch (exception: Exception) {
                exception.printStackTrace()
            }
            return result
        }
    }

    fun onSwipeRight() {}
    fun onSwipeLeft() {}
    fun onSwipeTop() {}
    fun onSwipeBottom() {}

    init {
        gestureDetector = GestureDetector(ctx, GestureListener())
    }

    companion object {
        private const val SWIPE_THRESHOLD = 100
        private const val SWIPE_VELOCITY_THRESHOLD = 100
    }
}