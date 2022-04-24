package com.example.Sleepy.main.modules.settings.presentation

import android.content.Context
import com.example.Sleepy.main.modules.settings.domain.SettingsViewModel
import android.media.AudioManager
import com.example.Sleepy.shared.MyPreferences.SettingsApp
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.example.Sleepy.R
import android.content.DialogInterface
import com.google.android.material.snackbar.Snackbar
import android.widget.RadioGroup
import com.google.android.material.slider.Slider
import com.google.android.material.bottomsheet.BottomSheetBehavior
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.example.Sleepy.databinding.FragmentSettingsBinding
import com.example.Sleepy.shared.AppTheme
import com.example.Sleepy.shared.MyAnimator
import com.example.Sleepy.shared.MyVibrator
import com.shawnlin.numberpicker.NumberPicker
import java.lang.Exception

class SettingsFragment : Fragment() {
    private lateinit var settingsViewModel: SettingsViewModel
    private lateinit var binding: FragmentSettingsBinding
    private lateinit var amAlarm: AudioManager
    private lateinit var handler: Handler
    private lateinit var prefs: SettingsApp

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        settingsViewModel = ViewModelProvider(this)[SettingsViewModel::class.java]
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        init()
        MyAnimator.setFadeAnimation(root)

        binding.bSettingsDefault.setOnClickListener {
            MaterialAlertDialogBuilder(
                requireContext()
            )
                .setTitle(getString(R.string.title_alert_setting))
                .setMessage(getString(R.string.message_alert_setting))
                .setNegativeButton(getString(R.string.neg_b_alert_setting)) { dialogInterface: DialogInterface, _: Int -> dialogInterface.cancel() }
                .setPositiveButton(getString(R.string.pos_b_alert_setting)) { _: DialogInterface?, _: Int ->
                    clearSharedPreferences()
                    init()
                    Snackbar.make(
                        binding.clSettingsBg,
                        getString(R.string.settings_clear),
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
                .show()
        }

        binding.sSleepTime.setOnClickListener {
            try {
                binding.npTimeSleep.isEnabled = binding.sSleepTime.isChecked
                MyVibrator.vibrate(30, requireContext())
                prefs.isCheckedSleepTime = binding.sSleepTime.isChecked
                binding.npTimeSleep.value = 0
                prefs.sleepTime = 0
            } catch (ex: Exception) {
                errorPlay()
            }
        }

        binding.rgTheme.setOnCheckedChangeListener { _: RadioGroup?, i: Int ->
            try {
                prefs.themeId = i
                AppTheme.setShareTheme(requireContext())
                MyVibrator.vibrate(30, requireContext())
//                when (i) {
//                    R.id.rbThemeAuto -> prefs.themeId = R.id.rbThemeAuto
//                    R.id.rbThemeDark -> prefs.themeId = R.id.rbThemeDark
//                    R.id.rbThemePurple -> prefs.themeId = R.id.rbThemePurple
//                }
                //fixme requireActivity().recreate()
            } catch (ex: Exception) {
                errorPlay()
            }
        }

        binding.sAnimations.setOnClickListener {
            try {
                MyVibrator.vibrate(30, requireContext())
                prefs.isAnimated = binding.sAnimations.isChecked
                //fixme requireActivity().recreate()
            } catch (ex: Exception) {
                errorPlay()
            }
        }

        binding.npCycles.setOnValueChangedListener { _: NumberPicker?, _: Int, newVal: Int ->
            try {
                MyVibrator.vibrate(15, requireContext())
                prefs.cardCount = newVal
            } catch (ex: Exception) {
                errorPlay()
            }
        }

        binding.npDurationCycle.setOnValueChangedListener { _: NumberPicker?, _: Int, newVal: Int ->
            try {
                MyVibrator.vibrate(10, requireContext())
                prefs.cycleDuration = newVal
            } catch (ex: Exception) {
                errorPlay()
            }
        }

        binding.npTimeSleep.setOnValueChangedListener { _: NumberPicker?, _: Int, newVal: Int ->
            try {
                MyVibrator.vibrate(15, requireContext())
                if (binding.sSleepTime.isChecked) prefs.sleepTime = newVal
                else prefs.sleepTime = 0
            } catch (ex: Exception) {
                errorPlay()
            }
        }

        binding.sTimeFormat.setOnClickListener {
            try {
                MyVibrator.vibrate(30, requireContext())
                prefs.set24TimeFormat(binding.sTimeFormat.isChecked)
            } catch (ex: Exception) {
                errorPlay()
            }
        }

        binding.sQuoteShow.setOnClickListener {
            try {
                MyVibrator.vibrate(30, requireContext())
                prefs.isCheckedQuotes = binding.sQuoteShow.isChecked
            } catch (ex: Exception) {
                errorPlay()
            }
        }

        binding.sVolAlarm.addOnChangeListener(Slider.OnChangeListener { _: Slider?, value: Float, _: Boolean ->
            try {
                MyVibrator.vibrate(30, requireContext())
                amAlarm.setStreamVolume(AudioManager.STREAM_ALARM, value.toInt(), 0)
            } catch (ex: Exception) {
                errorPlay()
            }
        })

        binding.sChoiceAlarm.setOnClickListener {
            try {
                MyVibrator.vibrate(30, requireContext())
                prefs.setCheckedBuiltinAlarm(binding.sChoiceAlarm.isChecked)
            } catch (ex: Exception) {
                errorPlay()
            }
        }

        binding.ivSettingsInfo.setOnClickListener {
            MyVibrator.vibrate(30, requireContext())
            if (BottomSheetBehavior.from(binding.incBottomSheet.flBottomSheet).state == BottomSheetBehavior.STATE_COLLAPSED) {
                BottomSheetBehavior.from(binding.incBottomSheet.flBottomSheet)
                    .setState(BottomSheetBehavior.STATE_EXPANDED)
            } else {
                BottomSheetBehavior.from(binding.incBottomSheet.flBottomSheet)
                    .setState(BottomSheetBehavior.STATE_COLLAPSED)
            }
        }

        binding.srlUpdate.setOnRefreshListener {
            init()
            MyVibrator.vibrate(30, requireContext())
            binding.srlUpdate.isRefreshing = false
        }

        binding.sVibration.setOnClickListener {
            try {
                MyVibrator.vibrate(30, requireContext())
                prefs.isVibrated = binding.sVibration.isChecked
            } catch (ex: Exception) {
                errorPlay()
            }
        }

        binding.npExtraTime.setOnValueChangedListener { _: NumberPicker?, _: Int, newVal: Int ->
            try {
                MyVibrator.vibrate(15, requireContext())
                prefs.extraTime = newVal
            } catch (ex: Exception) {
                errorPlay()
            }
        }

        return root
    }

    private fun init() {
        amAlarm = requireContext().getSystemService(Context.AUDIO_SERVICE) as AudioManager
        binding.sVolAlarm.value = amAlarm.getStreamVolume(AudioManager.STREAM_ALARM).toFloat()
        BottomSheetBehavior.from(binding.incBottomSheet.flBottomSheet).state = BottomSheetBehavior.STATE_COLLAPSED
        handler = Handler(Looper.getMainLooper())
        //fixme handler.removeCallbacks(run)
        //handler.post(run)
        shared
        setVolumeAlarm()
    }

    private fun setVolumeAlarm() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            binding.sVolAlarm.valueFrom = amAlarm.getStreamMinVolume(AudioManager.STREAM_ALARM).toFloat()
        }
        binding.sVolAlarm.valueTo = amAlarm.getStreamMaxVolume(AudioManager.STREAM_ALARM).toFloat()
    }

    private fun loadPlay() {
        binding.lCheckLoad.setAnimation(R.raw.check_mark_settings)
        if (!binding.lCheckLoad.isAnimating) binding.lCheckLoad.playAnimation()
    }

    private fun errorPlay() {
        binding.lCheckLoad.setAnimation(R.raw.exclamation_mark)
        if (!binding.lCheckLoad.isAnimating) binding.lCheckLoad.playAnimation()
    }

    private val shared: Unit
        get() {
            prefs = SettingsApp(requireContext())
            try {
                binding.rgTheme.check(prefs.themeId)
                binding.sAnimations.isChecked = prefs.isAnimated
                binding.npCycles.value = prefs.cardCount
                binding.sSleepTime.isChecked = prefs.isCheckedSleepTime
                binding.npTimeSleep.isEnabled = binding.sSleepTime.isChecked
                binding.npTimeSleep.value = prefs.sleepTime
                binding.sTimeFormat.isChecked = prefs.is24TimeFormat()
                binding.npDurationCycle.value = prefs.cycleDuration
                binding.sQuoteShow.isChecked = prefs.isCheckedQuotes
                binding.sChoiceAlarm.isChecked = prefs.isBuiltinAlarm
                binding.sVibration.isChecked = prefs.isVibrated
                binding.npExtraTime.value = prefs.extraTime
                loadPlay()
                Log.i("LOADING_SETTINGS", "Ok")
            } catch (ex: Exception) {
                errorPlay()
                Log.i("LOADING_SETTINGS", "init_error")
            }
        }

    private val run: Runnable = object : Runnable {
        override fun run() {
            try {
                binding.sVolAlarm.value = amAlarm.getStreamVolume(AudioManager.STREAM_ALARM).toFloat()
            } catch (ex: Exception) {
                Log.i("SETTINGS", "setVolume$ex")
            }
            handler.postDelayed(this, 1000)
        }
    }

    private fun clearSharedPreferences() {
        prefs.clearSettingsApp()
    }

    override fun onPause() {
        handler.removeCallbacks(run)
        super.onPause()
    }

    override fun onResume() {
        handler.post(run)
        super.onResume()
    }
}