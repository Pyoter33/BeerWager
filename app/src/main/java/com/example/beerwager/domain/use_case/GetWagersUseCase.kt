package com.example.beerwager.domain.use_case

import com.example.beerwager.data.data_source.Wager
import com.example.beerwager.domain.models.WagerCategory
import com.example.beerwager.domain.repository.WagerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetWagersUseCase @Inject constructor(private val wagerRepository: WagerRepository, private val wagerFiltersUtil: WagerFiltersUtil) {

    operator fun invoke(filters: List<WagerCategory>): Flow<Map<WagerCategory, List<Wager>>> {
        return wagerRepository.getWagers().map { wagers ->
            val activeFilters = if (filters.isEmpty()) WagerCategory.entries else filters
            return@map activeFilters.associateWith { category ->
                when (category) {
                    WagerCategory.CLOSED -> wagers.filter { wagerFiltersUtil.isClosed(it) }
                    WagerCategory.UPCOMING -> wagers.filter { wagerFiltersUtil.isUpcoming(it) }
                    WagerCategory.FUTURE -> wagers.filter { wagerFiltersUtil.isFuture(it) }
                }.sortedBy { it.date }
            }
        }
    }
}