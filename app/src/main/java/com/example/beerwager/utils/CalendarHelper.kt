package com.example.beerwager.utils

import android.content.ContentValues
import android.content.Context
import android.provider.CalendarContract
import com.example.beerwager.data.data_source.Wager
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CalendarHelper @Inject constructor(@ApplicationContext private val context: Context) {

    fun addWagerReminder(wager: Wager) {
        val contentResolver = context.contentResolver
        val defaultTimeZone = TimeZone.getDefault()
        val date = Date.from(wager.date.atStartOfDay(ZoneId.systemDefault()).toInstant());
        val rawOffsetMillis = defaultTimeZone.rawOffset
        val dstOffsetMillis = if (defaultTimeZone.inDaylightTime(date)) defaultTimeZone.dstSavings else 0

        val values = ContentValues().apply {
            if (wager.time == null) {
                put(CalendarContract.Events.ALL_DAY, true)
            }
            val start = wager.date.atTime(wager.time ?: LocalTime.of(12, 0))
            put(CalendarContract.Events.DTSTART, start.toInstant(ZoneOffset.UTC).toEpochMilli() - rawOffsetMillis - dstOffsetMillis)
            put(
                CalendarContract.Events.DTEND,
                start.plusHours(1).toInstant(ZoneOffset.UTC).toEpochMilli() - rawOffsetMillis - dstOffsetMillis
            )
            put(CalendarContract.Events.TITLE, wager.title)
            put(CalendarContract.Events.DESCRIPTION, wager.description)
            put(CalendarContract.Events.CALENDAR_ID, 1)
            put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getTimeZone("UTC").id)
        }
        contentResolver.insert(CalendarContract.Events.CONTENT_URI, values)
    }

}