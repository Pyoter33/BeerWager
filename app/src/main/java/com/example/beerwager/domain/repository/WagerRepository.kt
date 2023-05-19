package com.example.beerwager.domain.repository

import com.example.beerwager.data.data_source.Wager
import kotlinx.coroutines.flow.Flow

interface WagerRepository {

    fun getWagers(): Flow<List<Wager>>

    suspend fun getWagerById(id: Long): Wager?

    suspend fun updateWager(wager: Wager)

    suspend fun createWager(wager: Wager)

    suspend fun closeWager(wagerId: Long)
    suspend fun deleteWager(wagerId: Long)
}