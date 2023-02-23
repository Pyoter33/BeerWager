package com.example.beerwager.domain.use_case

import com.example.beerwager.data.data_source.Wager
import com.example.beerwager.domain.models.WagerFilter
import com.example.beerwager.domain.repository.WagerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetWagersBySearchUseCase @Inject constructor(private val repository: WagerRepository, private val wagerFiltersUtil: WagerFiltersUtil) {

    operator fun invoke(query: String): Flow<Map<String, List<Wager>>> {
        return repository.getWagers().map { wagers ->
            val filteredList = wagers.filter { it.title.lowercase().contains(query.lowercase()) }
            return@map mapOf(
                WagerFilter.CLOSED.toString() to wagerFiltersUtil.filterClosed(filteredList),
                WagerFilter.UPCOMING.toString() to wagerFiltersUtil.filterUpcoming(filteredList),
                WagerFilter.FUTURE.toString() to wagerFiltersUtil.filterFuture(filteredList)
            )
        }
    }

}