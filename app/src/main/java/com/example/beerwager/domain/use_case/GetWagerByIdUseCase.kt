package com.example.beerwager.domain.use_case

import com.example.beerwager.data.data_source.Wager
import com.example.beerwager.domain.models.WagerCategory
import com.example.beerwager.domain.repository.WagerRepository
import javax.inject.Inject

class GetWagerByIdUseCase @Inject constructor(
    private val repository: WagerRepository,
    private val wagerFiltersUtil: WagerFiltersUtil
) {

    suspend operator fun invoke(id: Long): Pair<WagerCategory, Wager>? {
        val wager = repository.getWagerById(id)
        val category = when {
            wager == null -> null
            wagerFiltersUtil.isClosed(wager) -> WagerCategory.CLOSED
            wagerFiltersUtil.isUpcoming(wager) -> WagerCategory.UPCOMING
            wagerFiltersUtil.isFuture(wager) -> WagerCategory.FUTURE
            else -> null
        }
        return if (wager != null && category != null) {
            Pair(category, wager)
        } else {
            null
        }
    }
}