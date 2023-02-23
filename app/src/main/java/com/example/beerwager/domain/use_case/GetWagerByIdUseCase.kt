package com.example.beerwager.domain.use_case

import com.example.beerwager.data.data_source.Wager
import com.example.beerwager.domain.repository.WagerRepository
import javax.inject.Inject

class GetWagerByIdUseCase @Inject constructor(private val repository: WagerRepository) {

    suspend operator fun invoke(id: Long): Wager? {
        return repository.getWagerById(id)
    }

}