package com.example.beerwager.domain.use_case

import com.example.beerwager.domain.repository.WagerRepository
import javax.inject.Inject

class CloseWagerUseCase @Inject constructor(private val repository: WagerRepository) {

    suspend operator fun invoke(wagerId: Long) {
        repository.closeWager(wagerId)
    }
}