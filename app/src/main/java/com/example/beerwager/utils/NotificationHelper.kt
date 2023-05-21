package com.example.beerwager.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.example.beerwager.data.data_source.Wager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationHelper @Inject constructor(@ApplicationContext private val context: Context) {

    private val alarmManager by lazy { context.getSystemService(Context.ALARM_SERVICE) as AlarmManager }

    fun scheduleNotification(wager: Wager, wagerId: Long) {
        val time = wager.date.atTime(wager.time ?: CalendarHelper.BASE_TIME)
        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            time.toEpochMillis(),
            cratePendingIntent(wager, wagerId)
        )
    }

    fun deleteNotification(wager: Wager, wagerId: Long) {
        alarmManager.cancel(cratePendingIntent(wager, wagerId))
    }

    private fun cratePendingIntent(wager: Wager, wagerId: Long): PendingIntent {
        val alarmIntent = Intent(context, AlarmReceiver::class.java)
        alarmIntent.putExtra(EXTRA_ID, wagerId)
        alarmIntent.putExtra(EXTRA_TITLE, wager.title)
        return PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
    }

    companion object {
        const val EXTRA_ID = "wagerId"
        const val EXTRA_TITLE = "title"
    }
}