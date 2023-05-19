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

    @Query(DELETE_BY_ID_QUERY)
    suspend fun deleteWager(id: Long)

    @Query(CLOSE_QUERY)
    suspend fun closeWager(id: Long)

    companion object {
        private const val GET_ALL_QUERY = "SELECT * FROM wagers"
        private const val GET_BY_ID_QUERY = "SELECT * FROM wagers WHERE id = :id"
        private const val DELETE_BY_ID_QUERY = "DELETE FROM wagers WHERE id = :id"
        private const val CLOSE_QUERY = "UPDATE wagers SET isClosed = 1 WHERE id = :id"
    }
}