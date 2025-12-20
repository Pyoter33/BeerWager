package com.example.beerwager.data.data_source

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.beerwager.ui.theme.Blue
import com.example.beerwager.ui.theme.BlueBackground
import com.example.beerwager.ui.theme.Green
import com.example.beerwager.ui.theme.GreenBackground
import com.example.beerwager.ui.theme.Orange
import com.example.beerwager.ui.theme.OrangeBackground
import com.example.beerwager.ui.theme.Pink
import com.example.beerwager.ui.theme.PinkBackground
import com.example.beerwager.ui.theme.Purple
import com.example.beerwager.ui.theme.PurpleBackground
import com.example.beerwager.ui.theme.Yellow
import com.example.beerwager.ui.theme.YellowBackground
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
    val id: Long = 0L
) {
    companion object {
        val WAGER_COLORS = listOf(Blue, Pink, Green, Orange, Yellow, Purple)
        val WAGER_BACKGROUND_COLORS = listOf(BlueBackground, PinkBackground, GreenBackground, OrangeBackground, YellowBackground, PurpleBackground)
    }
}