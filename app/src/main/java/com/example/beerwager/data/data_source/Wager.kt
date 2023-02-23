package com.example.beerwager.data.data_source

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.beerwager.ui.theme.*
import java.time.LocalDate
import java.time.LocalTime

@Entity(tableName = "wagers")
data class Wager(
    val title: String,
    val description: String,
    val date: LocalDate,
    val time: LocalTime?,
    val colour: Int,
    val wagerers: List<String>,
    val beersAtStake: Int,
    val hasNotification: Boolean,
    val isClosed: Boolean = false,
    @PrimaryKey(autoGenerate = true)
    val id: Long? = null
) {
    companion object {
        val WAGER_COLORS = listOf(Blue, Pink, Orange, Yellow, Purple)
    }
}