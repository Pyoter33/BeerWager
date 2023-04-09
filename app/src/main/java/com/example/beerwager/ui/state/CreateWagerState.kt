package com.example.beerwager.ui.state

import androidx.annotation.StringRes
import java.time.LocalDate
import java.time.LocalTime

data class CreateWagerState(
    val isInfoDisplayed: Boolean = false,
    val beersAtStake: Int = 0,
    val title: String = "",
    @StringRes val titleError: Int? = null,
    val description: String = "",
    @StringRes val descriptionError: Int? = null,
    val date: LocalDate? = null,
    @StringRes val dateError: Int? = null,
    val time: LocalTime? = null,
    val hasNotification: Boolean = false,
    val isInCalendar: Boolean = false,
    val colour: Int = 0,
    val wagererName: String = "",
    val wagerers: List<String> = emptyList(),
    @StringRes val wagerersError: Int? = null
)