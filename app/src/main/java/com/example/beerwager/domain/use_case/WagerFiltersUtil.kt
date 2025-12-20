package com.example.beerwager.domain.use_case

import com.example.beerwager.data.data_source.Wager
import java.time.LocalDate
import javax.inject.Inject

class WagerFiltersUtil @Inject constructor() {

    fun isClosed(wager: Wager): Boolean {
        return wager.isClosed
    }

    fun isUpcoming(wager: Wager): Boolean {
        return !wager.isClosed && (wager.date.isBefore(LocalDate.now()) || wager.date.isEqual(LocalDate.now()))
    }

    fun isFuture(wager: Wager): Boolean {
        return !wager.isClosed && wager.date.isAfter(LocalDate.now())
    }
}
