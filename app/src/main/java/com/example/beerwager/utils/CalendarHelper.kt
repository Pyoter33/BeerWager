package com.example.beerwager.utils

import android.content.ContentValues
import android.content.Context
import android.provider.CalendarContract
import com.example.beerwager.data.data_source.Wager
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.LocalTime
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CalendarHelper @Inject constructor(@ApplicationContext private val context: Context) {

    fun addWagerReminder(wager: Wager) {
        val contentResolver = context.contentResolver
        val values = ContentValues().apply {
            if (wager.time == null) {
                put(CalendarContract.Events.ALL_DAY, true)
            }
            val start = wager.date.atTime(wager.time ?: BASE_TIME)
            put(CalendarContract.Events.DTSTART, start.toEpochMillis())
            put(
                CalendarContract.Events.DTEND,
                start.plusHours(1).toEpochMillis()
            )
            put(CalendarContract.Events.TITLE, wager.title)
            put(CalendarContract.Events.DESCRIPTION, wager.description)
            put(CalendarContract.Events.CALENDAR_ID, 1)
            put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getTimeZone(TIME_ZONE_ID).id)
        }
        contentResolver.insert(CalendarContract.Events.CONTENT_URI, values)
    }

    companion object {
        private const val TIME_ZONE_ID = "UTC"
        val BASE_TIME: LocalTime = LocalTime.of(12, 0)
    }
}