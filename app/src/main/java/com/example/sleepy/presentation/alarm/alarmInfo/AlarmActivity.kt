package com.example.sleepy.presentation.alarm.alarmInfo

import androidx.appcompat.app.AppCompatActivity
import android.media.MediaPlayer
import android.media.Ringtone
import android.os.PowerManager.WakeLock
import android.media.AudioAttributes
import android.os.Bundle
import com.example.sleepy.R
import android.media.AudioManager
import com.example.sleepy.utils.AlarmUtils
import android.media.RingtoneManager
import android.os.Build
import android.os.PowerManager
import android.view.WindowManager
import com.example.sleepy.utils.Quotes
import android.content.Intent
import android.net.Uri
import android.util.Log
import com.example.sleepy.data.storage.PrefsStorage
import com.example.sleepy.databinding.ActivityAlarmBinding
import com.example.sleepy.presentation.splash.SplashActivity
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class AlarmActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAlarmBinding
    private var mpRingtone: MediaPlayer? = MediaPlayer()
    private lateinit var ringtone: Ringtone
    private lateinit var notificationUri: Uri
    private lateinit var mWakeLock: WakeLock
    private lateinit var aaAlarmType: AudioAttributes
    private lateinit var prefs: PrefsStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm)
        binding = ActivityAlarmBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        playSound()
        binding.bStopAlarm.setOnClickListener { finish() }
        binding.bPlusTime.setOnClickListener { setAlarmWithExtraTime() }
    }

    private fun init() {
        prefs = PrefsStorage(this)
        binding.bPlusTime.text = getString(R.string.add_more_time, prefs.extraTime)
        supportActionBar?.hide()
        volumeControlStream = AudioManager.STREAM_ALARM
        setAlarmType()
        setQuoteAndTime()
    }

    private fun setAlarmWithExtraTime() {
        val c = Calendar.getInstance()
        c.add(Calendar.MINUTE, prefs.extraTime)
        finish()
        AlarmUtils.setAlarm(this, c, binding.rlMainAlarm)
    }

    private fun setAlarmType() {
        aaAlarmType = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_ALARM)
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .build()
        notificationUriAlarm
    }

    private val notificationUriAlarm: Unit
        get() {
            notificationUri = RingtoneManager.getActualDefaultRingtoneUri(
                applicationContext,
                RingtoneManager.TYPE_ALARM
            )
            Log.i("alarm", "type_alarm")

//            if (notificationUri.) {
//                notificationUri = RingtoneManager.getActualDefaultRingtoneUri(
//                    applicationContext,
//                    RingtoneManager.TYPE_RINGTONE
//                )
//                Log.i("alarm", "type_ringtone")
//                if (notificationUri == null) {
//                    notificationUri = RingtoneManager.getActualDefaultRingtoneUri(
//                        applicationContext,
//                        RingtoneManager.TYPE_NOTIFICATION
//                    )
//                    Log.i("alarm", "type_notification")
//                }
//            }

            setScreenFlags()
        }

    private fun playSound() {
        try {
            ringtone = RingtoneManager.getRingtone(applicationContext, notificationUri)
            ringtone.audioAttributes = aaAlarmType
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) ringtone.isLooping = true
            ringtone.play()

            if (!ringtone.isPlaying) {
                try {
                    mpRingtone = MediaPlayer.create(applicationContext, notificationUri)
                    mpRingtone!!.setAudioAttributes(aaAlarmType)
                } catch (ex: Exception) {
                    Log.i("alarm", "setAttributesMethod is null - $ex")
                }
                mpRingtone?.isLooping = true
                mpRingtone?.start()
                Log.i("alarm", "Start media Playing :|")
            } else {
                Log.i("alarm", "Start ringtone Playing :)")
            }
        } catch (ex: Exception) {
            finish()
            Log.i("alarm", "ringtone cannot play :( $ex")
        }
    }

    private fun setScreenFlags() {
        val pm = getSystemService(POWER_SERVICE) as PowerManager
        mWakeLock = pm.newWakeLock(
            PowerManager.SCREEN_BRIGHT_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP,
            "Sleepy:alarmScreen"
        )
        mWakeLock.acquire(10 * 60 * 1000L /*10 min*/)
        val win = window
        win.addFlags(
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                    or WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                    or WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
                    or WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON
        )
    }

    private fun setQuoteAndTime() {
        binding.tvAlarmTime.text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date().time)
        if (prefs.isCheckedQuotes) binding.tvAlarmInfo.text = Quotes.quoteAlarm
        else binding.tvAlarmInfo.text = "Будильник"
    }

    override fun onDestroy() {
        stopAlarm()
        startActivity(Intent(this, SplashActivity::class.java))
        super.onDestroy()
    }

    private fun stopAlarm() {
        if (mpRingtone != null && mpRingtone!!.isPlaying) {
            mpRingtone!!.stop()
            Log.i("alarm", "Stop media Playing :|")
        }
        if (ringtone.isPlaying) {
            ringtone.stop()
            Log.i("alarm", "Stop ringtone Playing :)")
        }
        if (mWakeLock.isHeld) {
            mWakeLock.release()
        }
    }
}