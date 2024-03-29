package com.example.sleepy.presentation.alarm

import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sleepy.R
import com.example.sleepy.databinding.FragmentAlarmBinding
import com.example.sleepy.presentation.sleep.recycler.SleepCards
import com.example.sleepy.presentation.sleep.recycler.SleepCardsAdapter
import com.example.sleepy.utils.AlarmUtils
import com.example.sleepy.utils.AnimationsUtils
import com.example.sleepy.utils.TimeUtils
import com.example.sleepy.utils.VibrationUtils
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.text.SimpleDateFormat
import java.util.*

class AlarmFragment : Fragment() {
    private lateinit var alarmViewModel: AlarmViewModel
    private lateinit var binding: FragmentAlarmBinding
    private val timeAlarm = Calendar.getInstance()
    private lateinit var alarmCards: ArrayList<SleepCards>
    private lateinit var alarmCardsAdapter: SleepCardsAdapter
    private lateinit var alarmManager: AlarmManager
    private lateinit var mtpTimeAlarm: MaterialTimePicker

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        alarmViewModel = ViewModelProvider(this)[AlarmViewModel::class.java]
        binding = FragmentAlarmBinding.inflate(inflater, container, false)
        val root: View = binding.root

        init()
        AnimationsUtils.setFadeAnimationStart(root)

        binding.fabAddAlarm.setOnClickListener {
            mtpTimeAlarm = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(12)
                .setMinute(0)
                .setTitleText("Выберите время")
                .build()
            mtpTimeAlarm.addOnPositiveButtonClickListener {
                timeAlarm[Calendar.MINUTE] = mtpTimeAlarm.minute
                timeAlarm[Calendar.HOUR_OF_DAY] = mtpTimeAlarm.hour
                setAlarm(timeAlarm, binding.clAlarm)
            }
            mtpTimeAlarm.show(childFragmentManager, "tag_picker")
        }

        binding.bCancelAlarm.setOnClickListener {
            try {
                val timeLastAlarm: Calendar = GregorianCalendar()
                timeLastAlarm.time = Date(alarmManager.nextAlarmClock.triggerTime)
                AlarmUtils.cancelAlarm(binding.clAlarm)
                initCardItem()
                alarmCards.clear()
                Snackbar.make(
                    binding.clAlarm,
                    getString(R.string.alarm_clock_has_been_canceled,
                        SimpleDateFormat("HH:mm", Locale.getDefault()).format(timeLastAlarm.time)),
                    Snackbar.LENGTH_LONG
                )
                    .setAction(R.string.return_alarm) {
                        setAlarm(
                            timeLastAlarm,
                            binding.clAlarm
                        )
                    }
                    .show()
            } catch (ex: Exception) {
                initCardItem()
                Snackbar.make(
                    binding.clAlarm,
                    R.string.alarm_canceled,
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }

        binding.srlUpdateAlarm.setOnRefreshListener {
            VibrationUtils.vibrate(30, requireContext())
            init()
            binding.srlUpdateAlarm.isRefreshing = false
        }

        return root
    }

    private fun init() {
        alarmCards = ArrayList()
        alarmCardsAdapter = SleepCardsAdapter(alarmCards)
        alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        initCardItem()
        permission
    }

    private val permission: Unit
        get() {
            if (!Settings.canDrawOverlays(activity)) {
                val intent = Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + requireActivity().packageName)
                )
                startActivity(intent)
            }
        }

    private fun setAlarm(time: Calendar, view: View) {
        AlarmUtils.setAlarm(requireContext(), time, view)
        initCardItem()
        Log.i("alarm", "Time Alarm - " + time[Calendar.HOUR_OF_DAY] + ":" + time[Calendar.MINUTE])
    }

    private fun initCardItem() {
        if(alarmManager.nextAlarmClock != null){
            binding.rvAlarmList.layoutManager = LinearLayoutManager(context)
            binding.rvAlarmList.adapter = alarmCardsAdapter
            binding.bCancelAlarm.visibility = View.VISIBLE
            binding.tvAlarmList.text = getString(R.string.alarm_list)
            alarmCards.clear()

            alarmCards.add(
                SleepCards(
                    SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(alarmManager.nextAlarmClock.triggerTime)),
                    getString(R.string.remaining_time,
                        TimeUtils.calcRemainingTimeMinute(
                            Date(alarmManager.nextAlarmClock.triggerTime),
                            requireContext())
                    )
                )
            )
        }else{
            binding.bCancelAlarm.visibility = View.GONE
            binding.tvAlarmList.text = getString(R.string.no_alarm)
        }

    }
}