package com.example.beerwager.utils

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.util.*

fun LocalDateTime.toEpochMillis(): Long {
    val defaultTimeZone = TimeZone.getDefault()
    val date = Date.from(this.toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant())
    val rawOffsetMillis = defaultTimeZone.rawOffset
    val dstOffsetMillis =
        if (defaultTimeZone.inDaylightTime(date)) defaultTimeZone.dstSavings else 0
    return this.toInstant(ZoneOffset.UTC).toEpochMilli() - rawOffsetMillis - dstOffsetMillis
}

