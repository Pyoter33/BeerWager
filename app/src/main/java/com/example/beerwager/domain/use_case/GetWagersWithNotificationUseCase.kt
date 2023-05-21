package com.example.beerwager.domain.use_case

import com.example.beerwager.data.data_source.Wager
import com.example.beerwager.domain.repository.WagerRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetWagersWithNotificationUseCase  @Inject constructor(private val wagerRepository: WagerRepository) {

    operator fun invoke(): Flow<List<Wager>> {
        return wagerRepository.getWagersWithActiveNotifications()
    }

}