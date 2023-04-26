package com.example.beerwager.ui.view_model

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beerwager.data.data_source.Wager
import com.example.beerwager.domain.use_case.*
import com.example.beerwager.ui.state.*
import com.example.beerwager.utils.NavigationArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateEditWagerViewModel @Inject constructor(
    private val getWagerByIdUseCase: GetWagerByIdUseCase,
    private val createWagerUseCase: CreateWagerUseCase,
    private val validateDescriptionUseCase: ValidateDescriptionUseCase,
    private val validateWagerersUseCase: ValidateWagerersUseCase,
    private val validateDateUseCase: ValidateDateUseCase,
    private val validateTitleUseCase: ValidateTitleUseCase,
    private val validateWagererNameUseCase: ValidateWagererNameUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var job: Job? = null

    private val _createWagerState by lazy {
        savedStateHandle.get<Long>(NavigationArgs.ARG_WAGER_ID)?.let {
            val state = MutableStateFlow(CreateWagerState())
            setWagersById(state, it)
            state
        } ?: MutableStateFlow(CreateWagerState())
    }
    val createWagerState = _createWagerState.asStateFlow()

    fun onEvent(event: CreateWagersEvent) {
        when (event) {
            is CreateEvent -> {
                validateAndCreateWager()
            }
            is AddWagererEvent -> {
                createWagerState.value.let { state ->
                    val validationResult = validateWagererNameUseCase(state.wagererName)
                    _createWagerState.value = if (validationResult.isSuccessful) {
                        state.copy(wagerers = state.wagerers + state.wagererName, wagerersError = null)
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
                if(event.value in 0..99) {
                    _createWagerState.value = createWagerState.value.copy(beersAtStake = event.value)
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
                _createWagerState.value = createWagerState.value.copy(date = event.date, dateError = null)
            }
            is DescriptionChangedEvent -> {
                _createWagerState.value =
                    createWagerState.value.copy(description = event.description, descriptionError = null)
            }
            is NotificationChangedEvent -> {
                _createWagerState.value =
                    createWagerState.value.copy(hasNotification = event.isChecked)
            }
            is TimeChangedEvent -> {
                _createWagerState.value = createWagerState.value.copy(time = event.time)
            }
            is TitleChangedEvent -> {
                _createWagerState.value = createWagerState.value.copy(title = event.title, titleError = null)
            }
            is WagererNameChangedEvent -> {
                _createWagerState.value = createWagerState.value.copy(wagererName = event.name)
            }
            is RemoveWagererEvent -> {
                _createWagerState.value = createWagerState.value.run {
                    copy(wagerers =
                    wagerers.filterIndexed { i, _ -> i != event.index })
                }
            }
        }
    }

    private fun validateAndCreateWager() {
        createWagerState.value.let { state ->
            val titleValidationResult = validateTitleUseCase(state.title)
            val descriptionValidationResult = validateDescriptionUseCase(state.description)
            val dateValidationResult = validateDateUseCase(state.date)
            val wagerersValidationResult = validateWagerersUseCase(state.wagerers)

            val hasError = listOf(
                titleValidationResult,
                descriptionValidationResult,
                dateValidationResult,
                wagerersValidationResult
            ).any { !it.isSuccessful }

            if (hasError) {
                _createWagerState.value = createWagerState.value.copy(
                    wagerersError = wagerersValidationResult.message,
                    titleError = titleValidationResult.message,
                    descriptionError = descriptionValidationResult.message,
                    dateError = dateValidationResult.message
                )
                return
            }

            viewModelScope.launch {
                createWagerUseCase(
                    Wager(
                        state.title,
                        state.description,
                        state.date!!,
                        state.time,
                        state.colour,
                        state.wagerers,
                        state.beersAtStake,
                        state.hasNotification
                    )
                )
            }
        }
    }

    private fun setWagersById(
        stateFlow: MutableStateFlow<CreateWagerState>,
        wagerId: Long
    ) {
        job?.cancel()
        job = viewModelScope.launch {
            val wager = getWagerByIdUseCase(wagerId)
            wager?.let {
                stateFlow.value = stateFlow.value.copy(
                    title = it.title,
                    description = it.description,
                    date = it.date,
                    time = it.time,
                    hasNotification = it.hasNotification,
                    colour = it.colour,
                    wagerers = it.wagerers
                )
            }
        }
    }
}