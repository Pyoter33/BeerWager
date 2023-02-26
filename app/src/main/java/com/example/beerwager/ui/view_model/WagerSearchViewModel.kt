package com.example.beerwager.ui.view_model

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beerwager.domain.use_case.GetWagersBySearchUseCase
import com.example.beerwager.ui.state.SearchEvent
import com.example.beerwager.ui.state.WagerSearchState
import com.example.beerwager.utils.NavigationArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WagerSearchViewModel @Inject constructor(
    private val getWagersBySearchUseCase: GetWagersBySearchUseCase,
    private val savedStateHande: SavedStateHandle
) : ViewModel() {

    private var job: Job? = null

    private val _wagerSearchState by lazy {
        val searchQuery = savedStateHande[NavigationArgs.ARG_SEARCH_QUERY] ?: NavigationArgs.ARG_EMPTY_STRING
        val state = MutableStateFlow(WagerSearchState(searchText = searchQuery))
        setWagersBySearch(state, searchQuery)
        state
    }
    val wagerSearchState = _wagerSearchState.asStateFlow()

    fun onEvent(event: SearchEvent) {
        setWagersBySearch(_wagerSearchState, event.query)
    }

    private fun setWagersBySearch(
        stateFlow: MutableStateFlow<WagerSearchState>,
        searchQuery: String
    ) {
        job?.cancel()
        job = viewModelScope.launch {
            getWagersBySearchUseCase(searchQuery).collect {
                stateFlow.value = stateFlow.value.copy(wagers = it, searchText = searchQuery)
            }
        }
    }

}