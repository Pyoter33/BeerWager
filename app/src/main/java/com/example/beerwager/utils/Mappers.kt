package com.example.beerwager.utils

import com.example.beerwager.data.data_source.Wager
import com.example.beerwager.data.dtos.LoginDto
import com.example.beerwager.data.dtos.RegisterDto
import com.example.beerwager.ui.state.CreateWagerState
import com.example.beerwager.ui.state.LoginRegisterState

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

fun LoginRegisterState.toLoginDto(): LoginDto {
    return LoginDto(username, password)
}

fun LoginRegisterState.toRegisterDto(): RegisterDto {
    return RegisterDto(username, email, password)
}