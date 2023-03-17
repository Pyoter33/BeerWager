package com.example.beerwager.ui.view_model

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beerwager.domain.use_case.*
import com.example.beerwager.ui.state.*
import com.example.beerwager.utils.NavigationArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class CreateEditWagerViewModel(
    private val getWagerByIdUseCase: GetWagerByIdUseCase,
    private val validateDescriptionUseCase: ValidateDescriptionUseCase,
    private val validateWagerersUseCase: ValidateWagerersUseCase,
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
        when(event) {
            is CreateEvent -> {}
            is AddWagererEvent -> {
                createWagerState.value.let {
                    //_createWagerState.value = it.copy(wagerers = it.wagerers + it.wagererName)
                }


            }
            is ShowInfoEvent -> {}
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