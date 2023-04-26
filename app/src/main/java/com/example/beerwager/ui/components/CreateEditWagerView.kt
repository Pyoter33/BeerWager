package com.example.beerwager.ui.components

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.beerwager.ui.state.BeersChangedEvent
import com.example.beerwager.ui.state.CreateWagerState
import com.example.beerwager.ui.state.CreateWagersEvent
import com.example.beerwager.ui.state.TitleChangedEvent
import kotlinx.coroutines.flow.StateFlow

@Composable
fun CreateEditWagerView(
    modifier: Modifier = Modifier,
    createWagerState: StateFlow<CreateWagerState>,
    onEvent: (CreateWagersEvent) -> Unit,
) {
    val scrollState by rememberSaveable { mutableStateOf(ScrollState(0)) }
    val state by createWagerState.collectAsState()
    Column(
        verticalArrangement = Arrangement.spacedBy(5.dp),
        modifier = Modifier
            .verticalScroll(scrollState)
            .padding(horizontal = 5.dp, vertical = 10.dp)
    ) {
        BeersAtStakeView(beersAtStake = state.beersAtStake, onBeersChanged = { onEvent(BeersChangedEvent(it)) })
        ShortTextView(label = "Title", text = state.title, onTextChanged = { onEvent(TitleChangedEvent(it)) })


    }
}