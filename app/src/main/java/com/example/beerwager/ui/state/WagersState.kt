package com.example.beerwager.ui.state

import com.example.beerwager.data.data_source.Wager
import com.example.beerwager.domain.models.WagerFilter

data class WagersState(
    val wagers: Map<String, List<Wager>> = emptyMap(),
    val activeFilters: List<WagerFilter> = emptyList(),
    val inSearchMode: Boolean = false
)