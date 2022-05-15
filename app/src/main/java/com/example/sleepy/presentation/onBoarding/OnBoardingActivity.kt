package com.example.sleepy.presentation.onBoarding

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.sleepy.utils.AppTheme
import com.example.sleepy.R
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.example.sleepy.utils.MyVibrator
import android.content.Intent
import com.example.sleepy.presentation.MainActivity
import android.media.AudioManager
import android.widget.LinearLayout
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.example.sleepy.databinding.ActivityOnBoardingBinding
import com.example.sleepy.presentation.onBoarding.viewpager.OnBoarding
import com.example.sleepy.presentation.onBoarding.viewpager.OnBoardingAdapter
import java.util.ArrayList

class OnBoardingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOnBoardingBinding
    private lateinit var states: ArrayList<OnBoarding>
    private lateinit var boardAdapter: OnBoardingAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppTheme.setShareTheme(applicationContext)
        setContentView(R.layout.activity_on_boarding)
        binding = ActivityOnBoardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        initCardItem()
        setCurrentIndicator(0)
        setOnBoardingIndicators()

        binding.vpDemo.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setCurrentIndicator(position)
            }
        })

        binding.bNext.setOnClickListener {
            MyVibrator.vibrate(30, this@OnBoardingActivity)

            if (binding.vpDemo.currentItem + 1 < boardAdapter.itemCount) {
                binding.vpDemo.currentItem = binding.vpDemo.currentItem + 1
            } else {
                binding.bNext.isEnabled = false
                startActivity(Intent(applicationContext, MainActivity::class.java))
                finish()
            }
        }

        binding.bSkip.setOnClickListener {
            binding.bSkip.isEnabled = false
            MyVibrator.vibrate(30, this@OnBoardingActivity)
            startActivity(Intent(applicationContext, MainActivity::class.java))
            finish()
        }
    }

    private fun init() {
        volumeControlStream = AudioManager.STREAM_ALARM
        states = ArrayList()
        boardAdapter = OnBoardingAdapter(states)
        binding.vpDemo.adapter = boardAdapter
    }

    private fun initCardItem() {
        val item1 = OnBoarding()
        item1.title = getString(R.string.app_name)
        item1.description = getString(R.string.item1_description)
        item1.image = R.drawable.board_1_svg

        val item2 = OnBoarding()
        item2.title = getString(R.string.app_name)
        item2.description = getString(R.string.item2_description)
        item2.image = R.drawable.board_2_svg

        val item3 = OnBoarding()
        item3.title = getString(R.string.app_name)
        item3.description = getString(R.string.item3_description)
        item3.image = R.drawable.board_3_svg

        states.add(item1)
        states.add(item2)
        states.add(item3)

        boardAdapter = OnBoardingAdapter(states)
    }

    private fun setOnBoardingIndicators() {
        val indicators = arrayOfNulls<ImageView>(boardAdapter.itemCount)
        val lParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        lParams.setMargins(8, 0, 8, 0)

        for (i in indicators.indices) {
            indicators[i] = ImageView(applicationContext)
            indicators[i]!!.setImageDrawable(
                ContextCompat.getDrawable(
                    applicationContext,
                    R.drawable.board_ind_active //active
                )
            )
            indicators[i]!!.layoutParams = lParams
            binding.llIndicators.addView(indicators[i])
        }
    }

    private fun setCurrentIndicator(ind: Int) {
        val childCount = binding.llIndicators.childCount
        for (i in 0 until childCount) {
            val iv = binding.llIndicators.getChildAt(i) as ImageView

            if (i == ind) {
                iv.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.board_ind_active //active
                    )
                )
            } else {
                iv.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.board_ind_inactive //inactive
                    )
                )
            }
        }

        if (ind == boardAdapter.itemCount - 1) binding.bNext.setText(R.string.board_start_activity)
        else binding.bNext.setText(R.string.board_next)
    }
}