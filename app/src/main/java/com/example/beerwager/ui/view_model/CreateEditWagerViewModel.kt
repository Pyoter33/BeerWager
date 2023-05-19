package com.example.beerwager.ui.view_model

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beerwager.data.data_source.Wager
import com.example.beerwager.domain.use_case.*
import com.example.beerwager.ui.state.*
import com.example.beerwager.utils.CalendarHelper
import com.example.beerwager.utils.NavigationArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class CreateEditWagerViewModel @Inject constructor(
    private val getWagerByIdUseCase: GetWagerByIdUseCase,
    private val createWagerUseCase: CreateWagerUseCase,
    private val updateWagerUseCase: UpdateWagerUseCase,
    private val closeWagerUseCase: CloseWagerUseCase,
    private val deleteWagerUseCase: DeleteWagerUseCase,
    private val validateDescriptionUseCase: ValidateDescriptionUseCase,
    private val validateWagerersUseCase: ValidateWagerersUseCase,
    private val validateTitleUseCase: ValidateTitleUseCase,
    private val validateWagererNameUseCase: ValidateWagererNameUseCase,
    private val calendarHelper: CalendarHelper,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val wagerId: Long = savedStateHandle.get<Long>(NavigationArgs.ARG_WAGER_ID)
        ?: throw IllegalArgumentException("Null id provided.")

    private val category: String? = savedStateHandle.get<String>(NavigationArgs.ARG_CATEGORY)

    private var job: Job? = null

    private val _createWagerState by lazy {
        val state = MutableStateFlow(CreateWagerState())
        category?.let { category ->
            if (wagerId != -1L) setWagersById(state, wagerId, category)
        }
        state
    }
    val createWagerState = _createWagerState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<UIEvent?>(0)
    val uiEvent: SharedFlow<UIEvent?> = _uiEvent

    fun onEvent(event: CreateWagersEvent) {
        when (event) {
            is SubmitWagerEvent -> {
                validateAndSubmitWager()
            }
            is AddWagererEvent -> {
                createWagerState.value.let { state ->
                    val validationResult = validateWagererNameUseCase(state.wagererName)
                    _createWagerState.value = if (validationResult.isSuccessful) {
                        state.copy(
                            wagerers = state.wagerers + state.wagererName,
                            wagererName = "",
                            wagerersError = null
                        )
                    } else {
                        state.copy(wagerersError = validationResult.message)
                    }
                }
            }
            is ToggleInfoEvent -> {
                _createWagerState.value =
                    createWagerState.value.copy(isInfoDisplayed = event.isDisplayed)
            }
            is BeersChangedEvent -> {
                if (event.value in 0..MAX_BEERS) {
                    _createWagerState.value =
                        createWagerState.value.copy(beersAtStake = event.value)
                }
            }
            is CalendarChangedEvent -> {
                _createWagerState.value =
                    createWagerState.value.copy(isInCalendar = event.isChecked)
            }
            is ColourChangedEvent -> {
                _createWagerState.value = createWagerState.value.copy(colour = event.id)
            }
            is DateChangedEvent -> {
                _createWagerState.value = createWagerState.value.copy(date = event.date)
            }
            is DescriptionChangedEvent -> {
                _createWagerState.value =
                    createWagerState.value.copy(
                        description = event.description,
                        descriptionError = null
                    )
            }
            is NotificationChangedEvent -> {
                _createWagerState.value =
                    createWagerState.value.copy(hasNotification = event.isChecked)
            }
            is TimeChangedEvent -> {
                _createWagerState.value = createWagerState.value.copy(time = event.time)
            }
            is TitleChangedEvent -> {
                _createWagerState.value =
                    createWagerState.value.copy(title = event.title, titleError = null)
            }
            is WagererNameChangedEvent -> {
                _createWagerState.value = createWagerState.value.copy(wagererName = event.name)
            }
            is RemoveWagererEvent -> {
                _createWagerState.value = createWagerState.value.run {
                    copy(wagerers = wagerers.filterIndexed { i, _ -> i != event.index })
                }
            }
            is AllDayChangedEvent -> {
                _createWagerState.value =
                    createWagerState.value.copy(time = if (event.allDay) null else LocalTime.now())
            }
            EditUnlockedEvent -> {
                _createWagerState.value = createWagerState.value.copy(isBlocked = false)
            }
            DeleteWagerEvent -> {
                deleteWager()
            }
            CloseWagerEvent -> {
                closeWager()
            }
        }
    }

    private fun closeWager() {
        viewModelScope.launch(Dispatchers.IO) {
            closeWagerUseCase(wagerId)
            _uiEvent.emit(SaveWagerEvent)
        }
    }

    private fun deleteWager() {
        viewModelScope.launch(Dispatchers.IO) {
            deleteWagerUseCase(wagerId)
            _uiEvent.emit(SaveWagerEvent)
        }
    }

    private fun validateAndSubmitWager() {
            createWagerState.value.let { state ->
            val titleValidationResult = validateTitleUseCase(state.title)
            val descriptionValidationResult = validateDescriptionUseCase(state.description)
            val wagerersValidationResult = validateWagerersUseCase(state.wagerers)

            val hasError = listOf(
                titleValidationResult,
                descriptionValidationResult,
                wagerersValidationResult
            ).any { !it.isSuccessful }

            if (hasError) {
                _createWagerState.value = createWagerState.value.copy(
                    wagerersError = wagerersValidationResult.message,
                    titleError = titleValidationResult.message,
                    descriptionError = descriptionValidationResult.message
                )
                return
            }
            submitWager(state)
        }
    }

    private fun submitWager(state: CreateWagerState) {
        viewModelScope.launch(Dispatchers.IO) {
            val wager =
                Wager(
                    state.title,
                    state.description,
                    state.date,
                    state.time,
                    state.colour,
                    state.wagerers,
                    state.beersAtStake,
                    state.hasNotification
                )
            if (wagerId == -1L) {
                createWagerUseCase(wager)
            } else {
                updateWagerUseCase(wager.copy(id = wagerId))
            }
            if (state.isInCalendar) calendarHelper.addWagerReminder(wager)
            _uiEvent.emit(SaveWagerEvent)
        }
    }

    private fun setWagersById(
        stateFlow: MutableStateFlow<CreateWagerState>,
        wagerId: Long,
        category: String
    ) {
        job?.cancel()
        job = viewModelScope.launch(Dispatchers.IO) {
            val wager = getWagerByIdUseCase(wagerId)
            wager?.let {
                stateFlow.value = stateFlow.value.copy(
                    beersAtStake = it.beersAtStake,
                    title = it.title,
                    description = it.description,
                    date = it.date,
                    time = it.time,
                    hasNotification = it.hasNotification,
                    colour = it.colour,
                    wagerers = it.wagerers,
                    isBlocked = true,
                    category = category
                )
            }
        }
    }

    companion object {
        private const val MAX_BEERS = 99
    }
}