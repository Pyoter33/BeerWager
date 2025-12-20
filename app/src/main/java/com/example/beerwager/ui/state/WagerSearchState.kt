package com.example.beerwager.ui.state

import com.example.beerwager.data.data_source.Wager
import com.example.beerwager.domain.models.WagerCategory

data class WagerSearchState(
    val wagers: Map<WagerCategory, List<Wager>> = emptyMap(),
    val searchText: String = ""
)