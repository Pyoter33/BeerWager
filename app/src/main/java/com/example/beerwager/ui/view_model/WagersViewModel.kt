package com.example.beerwager.ui.view_model

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beerwager.domain.models.WagerFilter
import com.example.beerwager.domain.use_case.GetWagersUseCase
import com.example.beerwager.ui.state.FilterEvent
import com.example.beerwager.ui.state.WagersState
import com.example.beerwager.utils.NavigationArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WagersViewModel @Inject constructor(
    private val getWagersUseCase: GetWagersUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _wagersState by lazy {
        val state = MutableStateFlow(WagersState(searchText = savedStateHandle[NavigationArgs.ARG_SEARCH_QUERY] ?: ""))
        setWagers(state)
        state
    }
    val wagersState = _wagersState.asStateFlow()

    private var job: Job? = null

    fun onEvent(event: FilterEvent) {
        setWagers(_wagersState, event.filters)
    }
    fun setSearchQuery(searchQuery: String) {
        _wagersState.value = wagersState.value.copy(searchText = searchQuery)
    }
    private fun setWagers(
        stateFlow: MutableStateFlow<WagersState>,
        filters: List<WagerFilter> = emptyList()
    ) {
        job?.cancel()
        job = viewModelScope.launch {
            getWagersUseCase(filters).collect {
                stateFlow.value = stateFlow.value.copy(wagers = it, activeFilters = filters)
            }
        }
    }

}