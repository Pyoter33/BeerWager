package com.example.beerwager.ui.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beerwager.domain.models.WagerFilter
import com.example.beerwager.domain.use_case.GetWagersBySearchUseCase
import com.example.beerwager.domain.use_case.GetWagersUseCase
import com.example.beerwager.ui.state.WagersState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WagersViewModel @Inject constructor(
    private val getWagersUseCase: GetWagersUseCase,
    private val getWagersBySearchUseCase: GetWagersBySearchUseCase
) : ViewModel() {

    private val _notesState by lazy {
        val state = MutableStateFlow(WagersState())
        setWagers(state)
        state
    }
    val notesState = _notesState.asStateFlow()

    private var job: Job? = null

    fun onFilter(filters: List<WagerFilter>) {
        setWagers(_notesState, filters)
    }

    private fun setWagers(stateFlow: MutableStateFlow<WagersState>, filters: List<WagerFilter> = emptyList()) {
        job?.cancel()
        job = viewModelScope.launch {
            getWagersUseCase(filters).collect {
                stateFlow.value = WagersState(it, filters)
            }
        }
    }

}