package com.example.beerwager.ui.state

import com.example.beerwager.data.data_source.Wager

data class WagerSearchState(
    val wagers: Map<String, List<Wager>> = emptyMap(),
    val searchText: String = ""
)