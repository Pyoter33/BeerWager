package com.example.beerwager.ui.state

import com.example.beerwager.domain.models.WagerFilter

sealed class WagersEvent
class FilterEvent(val filters: List<WagerFilter>) : WagersEvent()
class SearchEvent(val query: String) : WagersEvent()
