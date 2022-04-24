package com.example.Sleepy.main.modules.onBoarding.domain

class OnBoarding {
    var title: String? = null
    var description: String? = null
    var image = 0
    var animation = 0
        private set

    fun setAnimationLottie(animation: Int) {
        this.animation = animation
    }
}