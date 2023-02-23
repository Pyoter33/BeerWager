package com.example.beerwager.domain.use_case

import com.example.beerwager.data.data_source.Wager
import com.example.beerwager.domain.repository.WagerRepository
import javax.inject.Inject

class CreateWagerUseCase @Inject constructor(private val wagerRepository: WagerRepository) {

    suspend operator fun invoke(wager: Wager) {
        wagerRepository.createWager(wager)
    }

}