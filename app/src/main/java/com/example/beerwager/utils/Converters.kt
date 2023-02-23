package com.example.beerwager.utils

import androidx.room.TypeConverter
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.time.LocalDate
import java.time.LocalTime

class Converters {

    @TypeConverter
    fun fromList(value: List<String>?) = Json.encodeToString(value)

    @TypeConverter
    fun toList(value: String): List<String>? = Json.decodeFromString(value)

    @TypeConverter
    fun toLocalDate(value: Long): LocalDate? = LocalDate.ofEpochDay(value)

    @TypeConverter
    fun fromLocalDate(date: LocalDate): Long = date.toEpochDay()

    @TypeConverter
    fun toLocalTime(value: Int?): LocalTime? = value?.toLong()?.let { LocalTime.ofSecondOfDay(it) }

    @TypeConverter
    fun fromLocalTime(time: LocalTime?): Int? = time?.toSecondOfDay()

}