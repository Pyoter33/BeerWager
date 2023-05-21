package com.example.beerwager.data.repository

import com.example.beerwager.data.data_source.Wager
import com.example.beerwager.data.data_source.WagerDao
import com.example.beerwager.domain.repository.WagerRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WagerRepositoryImpl @Inject constructor(private val dao: WagerDao): WagerRepository {

    override fun getWagers(): Flow<List<Wager>> {
        return dao.getWagers()
    }

    override fun getWagersWithActiveNotifications(): Flow<List<Wager>> {
        return dao.getWagersWithActiveNotifications()
    }

    override suspend fun getWagerById(id: Long): Wager? {
        return dao.getWagerById(id)
    }

    override suspend fun updateWager(wager: Wager) {
        dao.updateWager(wager)
    }

    override suspend fun createWager(wager: Wager): Long {
        return dao.createWager(wager)
    }

    override suspend fun closeWager(wagerId: Long) {
        dao.closeWager(wagerId)
    }

    override suspend fun deleteWager(wagerId: Long) {
        dao.deleteWager(wagerId)
    }
}