package com.example.beerwager.data.data_source

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface WagerDao {

    @Query(GET_ALL_QUERY)
    fun getWagers(): Flow<List<Wager>>

    @Query(GET_BY_ID_QUERY)
    suspend fun getWagerById(id: Long): Wager?

    @Update
    suspend fun updateWager(wager: Wager)

    @Insert
    suspend fun createWager(wager: Wager)

    @Delete
    suspend fun deleteWager(wager: Wager)

    companion object {
        const val GET_ALL_QUERY = "SELECT * FROM wagers"
        const val GET_BY_ID_QUERY = "SELECT * FROM wagers WHERE id = :id"
    }
}