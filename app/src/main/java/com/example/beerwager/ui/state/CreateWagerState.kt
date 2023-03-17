package com.example.beerwager.ui.state

import java.time.LocalDate
import java.time.LocalTime

data class CreateWagerState(
    val isInfoDisplayed: Boolean = false,
    val beersAtStake: Int = 0,
    val title: String = "",
    val description: String = "",
    val date: LocalDate = LocalDate.now(),
    val time: LocalTime? = null,
    val hasNotification: Boolean = false,
    val isInCalendar: Boolean = false,
    val colour: Int = 0,
    val wagererName: String = "",
    val wagerers: List<String> = emptyList()
)