package com.example.beerwager.ui.state

import com.example.beerwager.domain.models.WagerFilter
import java.time.LocalDate
import java.time.LocalTime

sealed class WagersEvent
data class FilterEvent(val filters: List<WagerFilter>) : WagersEvent()
data class SearchEvent(val query: String) : WagersEvent()

sealed class CreateWagersEvent
object SubmitWagerEvent : CreateWagersEvent()
object AddWagererEvent : CreateWagersEvent()
data class RemoveWagererEvent(val index: Int): CreateWagersEvent()
data class ToggleInfoEvent(val isDisplayed: Boolean) : CreateWagersEvent()
data class BeersChangedEvent(val value: Int) : CreateWagersEvent()
data class TitleChangedEvent(val title: String) : CreateWagersEvent()
data class DescriptionChangedEvent(val description: String) : CreateWagersEvent()
data class DateChangedEvent(val date: LocalDate) : CreateWagersEvent()
data class TimeChangedEvent(val time: LocalTime) : CreateWagersEvent()
data class NotificationChangedEvent(val isChecked: Boolean) : CreateWagersEvent()
data class CalendarChangedEvent(val isChecked: Boolean) : CreateWagersEvent()
data class ColourChangedEvent(val id: Int) : CreateWagersEvent()
data class WagererNameChangedEvent(val name: String) : CreateWagersEvent()
data class AllDayChangedEvent(val allDay: Boolean) : CreateWagersEvent()
object EditUnlockedEvent : CreateWagersEvent()
object DeleteWagerEvent : CreateWagersEvent()
object CloseWagerEvent : CreateWagersEvent()
sealed class UIEvent
object SaveWagerEvent: UIEvent()