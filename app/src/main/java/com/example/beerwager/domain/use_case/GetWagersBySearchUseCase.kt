package com.example.beerwager.domain.use_case

import com.example.beerwager.data.data_source.Wager
import com.example.beerwager.domain.models.WagerCategory
import com.example.beerwager.domain.repository.WagerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetWagersBySearchUseCase @Inject constructor(private val repository: WagerRepository, private val wagerFiltersUtil: WagerFiltersUtil) {

    operator fun invoke(query: String): Flow<Map<WagerCategory, List<Wager>>> {
        return repository.getWagers().map { wagers ->
            val filteredList = if (query.isNotEmpty()) wagers.filter { it.title.lowercase().contains(query, true) } else emptyList()
            return@map mapOf(
                WagerCategory.CLOSED to filteredList.filter { wagerFiltersUtil.isClosed(it) }.sortedBy { it.date },
                WagerCategory.UPCOMING to filteredList.filter { wagerFiltersUtil.isUpcoming(it) }.sortedBy { it.date },
                WagerCategory.FUTURE to filteredList.filter { wagerFiltersUtil.isFuture(it) }.sortedBy { it.date }
            )
        }
    }

}