package com.example.beerwager.domain.use_case

import com.example.beerwager.data.data_source.Wager
import com.example.beerwager.domain.repository.WagerRepository
import javax.inject.Inject

class UpdateWagerUseCase @Inject constructor(private val repository: WagerRepository) {

    suspend operator fun invoke(wager: Wager) {
        repository.updateWager(wager)
    }
}