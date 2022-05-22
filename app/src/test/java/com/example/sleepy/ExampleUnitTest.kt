package com.example.sleepy

import android.app.AlarmManager
import android.content.Context
import android.view.View
import androidx.test.core.app.ApplicationProvider
import com.example.sleepy.utils.AlarmUtils
import com.example.sleepy.utils.TimeUtils
import org.junit.Assert
import org.junit.Test
import org.mockito.Mockito.mock
import java.util.*

class ExampleUnitTest{

    @Test
    fun calcRemainingTimeMinute() {
        //теститрование функции расчета оставшегося времени
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, 1)
        Assert.assertEquals(
            TimeUtils.calcRemainingTimeMinute(
                date = Date(calendar.timeInMillis),
                context = mock(Context::class.java)
            ),
            "24null"
        )
    }

    @Test
    fun getFormatTime(){
        //тестироваие функции форматирования минут в строку
        val calendar = Calendar.getInstance()
        calendar.set(2022, 5, 8, 14, 15, 12)
        Assert.assertEquals(
            TimeUtils.getFormatTime(
                minute = calendar.get(Calendar.MINUTE).toLong(),
                context = mock(Context::class.java)
            ), "15null"
        )
    }
}