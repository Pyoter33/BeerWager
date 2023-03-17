package com.example.beerwager.ui.state

import com.example.beerwager.domain.models.WagerFilter

sealed class WagersEvent
data class FilterEvent(val filters: List<WagerFilter>) : WagersEvent()
data class SearchEvent(val query: String) : WagersEvent()

sealed class CreateWagersEvent
object CreateEvent : CreateWagersEvent()
object AddWagererEvent : CreateWagersEvent()
object ShowInfoEvent : CreateWagersEvent()