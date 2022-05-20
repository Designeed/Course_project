package com.example.sleepy.presentation.sleep

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.TimePicker
import androidx.core.widget.NestedScrollView
import com.google.android.material.snackbar.Snackbar
import com.example.sleepy.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import android.content.DialogInterface
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sleepy.data.storage.PrefsStorage
import com.example.sleepy.databinding.FragmentSleepBinding
import java.text.SimpleDateFormat
import java.util.*
import com.example.sleepy.presentation.MainActivity
import com.example.sleepy.presentation.sleep.recycler.SleepCards
import com.example.sleepy.presentation.sleep.recycler.SleepCardsAdapter
import com.example.sleepy.utils.*

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

    @SuppressLint("ClickableViewAccessibility")
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
        AnimationsUtils.setFadeAnimationStart(root)

        sleepViewModel.text.observe(
            viewLifecycleOwner,
            { s: String? -> binding.tvSetTime.text = s })

        sleepViewModel.getCurTime().observe(viewLifecycleOwner, { date: Calendar ->
            binding.tpSleep.hour = date[Calendar.HOUR_OF_DAY]
            binding.tpSleep.minute = date[Calendar.MINUTE]
            binding.tpSleep.setIs24HourView(true)
        })

        sleepViewModel.textSleep.observe(viewLifecycleOwner, { s: String? -> binding.tvGoToSleep.text = s })

        binding.bCalc.setOnClickListener {
            VibrationUtils.vibrate(30, requireContext())
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
            VibrationUtils.vibrate(15, requireContext())
        }

        binding.bClearTime.setOnClickListener {
            TimeUtils.clearTime(binding.tpSleep, requireContext())
        }

        binding.svMainSleep.setOnScrollChangeListener { _: NestedScrollView?, _: Int, scrollY: Int, _: Int, _: Int ->
            if (isAnimate) binding.lStarTimePicker.frame = scrollY / 8
        }

        binding.bAddAlarm.setOnClickListener {
            AlarmUtils.setAlarm(
                requireContext(),
                TimeUtils.getCurrentTime(binding.tpSleep),
                binding.clMain
            )
        }

        binding.lCatSleepMain.setOnClickListener {
            val q = Quotes.quoteCat
            val s = Snackbar.make(requireView(), q, Snackbar.LENGTH_LONG).setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE)
            if (q == getString(R.string.you_need_anecdote)) {
                s.setAction(R.string.yes) {
                    MaterialAlertDialogBuilder(
                        requireContext(),
                        R.style.ThemeOverlay_App_MaterialAlertDialog
                    )
                        .setTitle(getString(R.string.title_alert_cat))
                        .setMessage(Quotes.anecdote)
                        .setPositiveButton(getString(R.string.pos_b_alert)) { dialogInterface: DialogInterface, _: Int ->
                            dialogInterface.cancel()
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
        TimeUtils.getAsleepText(fallingAsleepTime, binding.tvTimeAsleep, requireContext())
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
            val prefs = PrefsStorage(requireContext())
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
        curTimeFull.add(Calendar.MINUTE, -fallingAsleepTime)
        remMinutes *= cardCount

        for (i in 0 until cardCount) {
            curTimeFull.add(Calendar.MINUTE, -minutesCycle)
            sleepCards.add(
                SleepCards(
                    sdf.format(curTimeFull.time),
                    getString(R.string.time_to_sleep,
                        TimeUtils.getFormatTime(
                        remMinutes.toLong(),
                        requireContext()
                    ))
                )
            )
            remMinutes -= cycleDuration
        }
        Log.i("initCard", "initCard")
    }

    private fun setTitleTime() {
        if (sleepCards.size >= 6)
            requireActivity().findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar).subtitle =
                (getString(R.string.optimal_time, sleepCards[sleepCards.size - 6].title))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        requireActivity().findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar).subtitle = ""
        sleepCards.clear()
    }
}