package com.example.beerwager.utils

import com.example.beerwager.data.data_source.Wager
import com.example.beerwager.ui.state.CreateWagerState

fun CreateWagerState.toWager(): Wager {
    return Wager(
        title,
        description,
        date,
        time,
        colour,
        wagerers,
        beersAtStake,
        hasNotification
    )
}