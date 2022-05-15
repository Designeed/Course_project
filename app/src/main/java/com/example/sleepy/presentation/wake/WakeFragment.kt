package com.example.sleepy.presentation.wake

import com.example.sleepy.presentation.MainActivity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.widget.TimePicker
import androidx.core.widget.NestedScrollView
import com.google.android.material.snackbar.Snackbar
import com.example.sleepy.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import android.content.DialogInterface
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.example.sleepy.utils.MyPreferences.SettingsApp
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sleepy.databinding.FragmentWakeBinding
import com.example.sleepy.presentation.wake.recycler.WakeCards
import com.example.sleepy.presentation.wake.recycler.WakeCardsAdapter
import com.example.sleepy.utils.MyAnimator
import com.example.sleepy.utils.MyTimer
import com.example.sleepy.utils.MyVibrator
import com.example.sleepy.utils.Quotes
import java.text.SimpleDateFormat
import java.util.*

class WakeFragment : Fragment() {
    private lateinit var wakeViewModel: WakeViewModel
    private lateinit var binding: FragmentWakeBinding
    private val curTime = Calendar.getInstance()
    private lateinit var timeCards: ArrayList<WakeCards>
    private lateinit var timeCardsAdapter: WakeCardsAdapter
    private lateinit var sdf: SimpleDateFormat
    private var cardCount = 6
    private var minutesCycle = -90
    private var remMinutes = 90
    private var cycleDuration = 0
    private var fallingAsleepTime = 0
    private lateinit var mainAct: MainActivity
    private var isAnimate = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        wakeViewModel = WakeViewModel(requireContext())
        binding = FragmentWakeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        init()
        initCardItem()
        MyAnimator.setFadeAnimationStart(root)

        wakeViewModel.text.observe(
            viewLifecycleOwner,
            { s: String? -> binding.tvSetTime.text = s })

        wakeViewModel.getCurTime().observe(viewLifecycleOwner, { date: Calendar ->
            binding.tpWake.hour = date[Calendar.HOUR_OF_DAY]
            binding.tpWake.minute = date[Calendar.MINUTE]
            binding.tpWake.setIs24HourView(true)
        })

        wakeViewModel.textWake.observe(
            viewLifecycleOwner,
            { s: String? -> binding.tvGoToSleep.text = s })

        binding.bClearTime.setOnClickListener {
            MyTimer.clearTime(binding.tpWake, requireContext())
        }

        binding.tpWake.setOnTimeChangedListener { _: TimePicker?, hourOfDay: Int, minute: Int ->
            val c: Calendar = GregorianCalendar()
            c[
                    Calendar.getInstance()[Calendar.YEAR],
                    Calendar.getInstance()[Calendar.MONTH],
                    Calendar.getInstance()[Calendar.DATE],
                    hourOfDay] = minute
            wakeViewModel.setCurTime(c)
            MyVibrator.vibrate(15, requireContext())
        }

        if (isAnimate) {
            binding.svMainWake.setOnScrollChangeListener { _: NestedScrollView?, _: Int, scrollY: Int, _: Int, _: Int ->
                binding.lStarTimePicker.frame = scrollY / 8
            }
        }

        binding.lYogaWake.setOnClickListener { view: View? ->
            val q = Quotes.quoteSloth
            val s = Snackbar.make(view!!, q, Snackbar.LENGTH_LONG).setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE)
            if (q == getString(R.string.you_need_fact)) {
                s.setAction(R.string.yes) {
                    MaterialAlertDialogBuilder(
                        requireContext()
                    )
                        .setTitle(getString(R.string.title_alert_sloth))
                        .setMessage(Quotes.fact)
                        .setPositiveButton(getString(R.string.pos_b_alert)) { dialogInterface: DialogInterface, _: Int -> dialogInterface.cancel() }
                        .show()
                }
            }
            s.show()
        }

        binding.bCalc.setOnClickListener {
            MyVibrator.vibrate(30, requireContext())
            remMinutes = cycleDuration
            setCurTime()
            initCardItem()
            setTitleTime()
        }

        return root
    }

    private fun init() {
        mainAct = MainActivity()
        timeCards = ArrayList()
        timeCardsAdapter = WakeCardsAdapter(timeCards)
        sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        shared
        setCurTime()
        remMinutes = cycleDuration
        minutesCycle = cycleDuration
        MyTimer.getAsleepText(fallingAsleepTime, binding.tvTimeAsleep, context!!)
        animations
    }

    private fun setCurTime() {
        curTime[
                Calendar.getInstance()[Calendar.YEAR],
                Calendar.getInstance()[Calendar.MONTH],
                Calendar.getInstance()[Calendar.DATE],
                binding.tpWake.hour] = binding.tpWake.minute
        curTime.add(Calendar.MINUTE, fallingAsleepTime)
    }

    private val animations: Unit
        get() {
            if (!isAnimate) binding.lYogaWake.speed = 0f
            else binding.lYogaWake.speed = 1f
        }

    private val shared: Unit
        get() {
            val prefs = SettingsApp(requireContext())
            cardCount = prefs.cardCount
            fallingAsleepTime = prefs.sleepTime
            cycleDuration = prefs.cycleDuration
            isAnimate = prefs.isAnimated
        }

    private fun initCardItem() {
        binding.rvCards.layoutManager = LinearLayoutManager(context)
        binding.rvCards.adapter = timeCardsAdapter
        timeCards.clear()

        for (i in 0 until cardCount) {
            curTime.add(Calendar.MINUTE, minutesCycle)
            timeCards.add(
                WakeCards(
                    sdf.format(curTime.time),
                    getString(
                        R.string.time_to_sleep,
                        MyTimer.getFormatTime(remMinutes.toLong(), requireContext())),
                    curTime
                )
            )
            remMinutes += cycleDuration
        }
        Log.i("initCard", "initCard_wake")
    }

    private fun setTitleTime() {
        if (timeCards.size >= 6)
            requireActivity().findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar).subtitle =
                (getString(R.string.optimal_time) + timeCards[5].time)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        requireActivity().findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar).subtitle = ""
        timeCards.clear()
    }
}