package com.example.beerwager.domain.use_case

import com.example.beerwager.data.data_source.Wager
import com.example.beerwager.domain.models.WagerFilter
import com.example.beerwager.domain.repository.WagerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetWagersUseCase @Inject constructor(private val wagerRepository: WagerRepository, private val wagerFiltersUtil: WagerFiltersUtil) {

    operator fun invoke(filters: List<WagerFilter>): Flow<Map<String, List<Wager>>> {
        return wagerRepository.getWagers().map { wagers ->
            if (filters.isEmpty()) {
                return@map mapOf(
                    WagerFilter.CLOSED.toString() to wagerFiltersUtil.filterClosed(wagers),
                    WagerFilter.UPCOMING.toString() to wagerFiltersUtil.filterUpcoming(wagers),
                    WagerFilter.FUTURE.toString() to wagerFiltersUtil.filterFuture(wagers)
                )
            }
            val wagerMap = mutableMapOf<String, List<Wager>>()
            if (WagerFilter.CLOSED in filters) {
                wagerMap[WagerFilter.CLOSED.toString()] = wagerFiltersUtil.filterClosed(wagers)
            }
            if (WagerFilter.UPCOMING in filters) {
                wagerMap[WagerFilter.UPCOMING.toString()] = wagerFiltersUtil.filterUpcoming(wagers)
            }
            if (WagerFilter.FUTURE in filters) {
                wagerMap[WagerFilter.FUTURE.toString()] = wagerFiltersUtil.filterFuture(wagers)
            }
            return@map wagerMap
        }
    }
}