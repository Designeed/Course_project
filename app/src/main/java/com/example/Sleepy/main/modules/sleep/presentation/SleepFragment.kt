package com.example.Sleepy.main.modules.sleep.presentation

import com.example.Sleepy.main.modules.sleep.domain.SleepViewModel
import com.example.Sleepy.main.modules.sleep.domain.SleepCards
import com.example.Sleepy.main.modules.sleep.domain.SleepCardsAdapter
import com.example.Sleepy.core.presentation.MainActivity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.widget.TimePicker
import androidx.core.widget.NestedScrollView
import com.google.android.material.snackbar.Snackbar
import com.example.Sleepy.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import android.content.DialogInterface
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.example.Sleepy.shared.MyPreferences.SettingsApp
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.Sleepy.databinding.FragmentSleepBinding
import com.example.Sleepy.shared.*
import java.text.SimpleDateFormat
import java.util.*

class SleepFragment : Fragment() {
    private lateinit var sleepViewModel: SleepViewModel
    private lateinit var binding: FragmentSleepBinding
    private val curTimeFull = Calendar.getInstance()
    private lateinit var sleepCards: ArrayList<SleepCards>
    private lateinit var sleepCardsAdapter: SleepCardsAdapter
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
        sleepViewModel = SleepViewModel(requireContext())
        binding = FragmentSleepBinding.inflate(inflater, container, false)
        val root: View = binding.root

        init()
        initCardItem()
        MyAnimator.setFadeAnimation(root)

        sleepViewModel.text.observe(
            viewLifecycleOwner,
            { s: String? -> binding.tvSetTime.text = s })

        sleepViewModel.getCurTime().observe(viewLifecycleOwner, { date: Calendar ->
            binding.tpSleep.hour = date[Calendar.HOUR_OF_DAY]
            binding.tpSleep.minute = date[Calendar.MINUTE]
            binding.tpSleep.setIs24HourView(true)
        })

        sleepViewModel.textSleep.observe(
            viewLifecycleOwner,
            { s: String? -> binding.tvGoToSleep.text = s })

        binding.bCalc.setOnClickListener {
            MyVibrator.vibrate(30, requireContext())
            remMinutes = cycleDuration
            curTimeFull[
                    Calendar.YEAR,
                    Calendar.MONTH,
                    Calendar.DATE,
                    binding.tpSleep.hour] = binding.tpSleep.minute
            initCardItem()
            setTitleTime()
        }

        binding.tpSleep.setOnTimeChangedListener { _: TimePicker?, hourOfDay: Int, minute: Int ->
            val c: Calendar = GregorianCalendar()
            c[Calendar.getInstance()[
                    Calendar.YEAR],
                    Calendar.getInstance()[Calendar.MONTH],
                    Calendar.getInstance()[Calendar.DATE],
                    hourOfDay] = minute
            sleepViewModel.setCurTime(c)
            MyVibrator.vibrate(15, requireContext())
        }

        binding.bClearTime.setOnClickListener {
            MyTimer.clearTime(binding.tpSleep, requireContext())
        }

        if (isAnimate) {
            binding.svMainSleep.setOnScrollChangeListener { _: NestedScrollView?, _: Int, scrollY: Int, _: Int, _: Int ->
                binding.lStarTimePicker.frame = scrollY / 8
            }
        }

        binding.bAddAlarm.setOnClickListener {
            MyAlarm.setAlarm(
                requireContext(), MyTimer.getCurrentTime(binding.tpSleep), binding.clMain
            )
        }

        binding.lCatSleepMain.setOnClickListener { view: View? ->
            val q = Quotes.quoteCat
            val s = Snackbar.make(view!!, q, Snackbar.LENGTH_LONG).setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE)
            if (q == getString(R.string.you_need_anecdote)) {
                s.setAction(R.string.yes) {
                    MaterialAlertDialogBuilder(
                        requireContext()
                    )
                        .setTitle(getString(R.string.title_alert_cat))
                        .setMessage(Quotes.anecdote)
                        .setPositiveButton(getString(R.string.pos_b_alert)) {
                                dialogInterface: DialogInterface, _: Int -> dialogInterface.cancel()
                        }
                        .show()
                }
            }
            s.show()
        }
        return root
    }

    private fun init() {
        sleepCards = ArrayList()
        sleepCardsAdapter = SleepCardsAdapter(sleepCards)
        mainAct = MainActivity()
        curTimeFull[
                Calendar.YEAR,
                Calendar.MONTH,
                Calendar.DATE,
                binding.tpSleep.hour] = binding.tpSleep.minute
        sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        shared
        remMinutes = cycleDuration
        minutesCycle = -cycleDuration
        MyTimer.getAsleepText(fallingAsleepTime, binding.tvTimeAsleep, requireContext())
        animations
    }

    private val animations: Unit
        get() {
            if (!isAnimate) {
                binding.lCatSleepMain.speed = 0f
            } else {
                binding.lCatSleepMain.speed = 1f
            }
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
        binding.rvCards.adapter = sleepCardsAdapter
        sleepCards.clear()
        curTimeFull.add(Calendar.MINUTE, minutesCycle * (cardCount + 1))
        curTimeFull.add(Calendar.MINUTE, fallingAsleepTime)
        remMinutes *= cardCount

        for (i in 0 until cardCount) {
            curTimeFull.add(Calendar.MINUTE, -minutesCycle)
            sleepCards.add(
                SleepCards(
                    sdf.format(curTimeFull.time),
                    getString(R.string.remaining_time) + MyTimer.getFormatTime(
                        remMinutes.toLong(),
                        requireContext()
                    )
                )
            )
            remMinutes -= cycleDuration
        }
        Log.i("initCard", "initCard")
    }

    private fun setTitleTime() {
        //if (sleepCards.size >= 6) mainAct.setTitleAppBar(getString(R.string.optimal_time) + sleepCards[sleepCards.size - 6].title)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        //MainActivity().setTitleAppBar("")
        sleepCards.clear()
    }
}