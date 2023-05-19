package com.example.beerwager.domain.use_case

import com.example.beerwager.data.data_source.Wager
import java.time.LocalDate
import javax.inject.Inject

class WagerFiltersUtil @Inject constructor() {

    fun filterClosed(wagers: List<Wager>): List<Wager> {
        return wagers.filter { it.isClosed }.sortedBy { it.date }
    }

    fun filterUpcoming(wagers: List<Wager>): List<Wager> {
        return wagers.filter { !it.isClosed && (it.date.isBefore(LocalDate.now()) || it.date.isEqual(LocalDate.now())) }.sortedBy { it.date }
    }

    fun filterFuture(wagers: List<Wager>): List<Wager> {
        return wagers.filter { !it.isClosed && it.date.isAfter(LocalDate.now()) }.sortedBy { it.date }
    }
}
