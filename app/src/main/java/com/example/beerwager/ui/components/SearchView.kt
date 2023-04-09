package com.example.beerwager.ui.components

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import com.example.beerwager.R
import com.example.beerwager.data.data_source.Wager
import com.example.beerwager.domain.models.WagerFilter
import com.example.beerwager.ui.state.SearchEvent
import com.example.beerwager.ui.state.WagerSearchState
import com.example.beerwager.ui.theme.White
import com.example.beerwager.utils.ColorValues
import com.example.beerwager.utils.Dimen
import kotlinx.coroutines.flow.StateFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchView(
    modifier: Modifier = Modifier,
    wagerSearchState: StateFlow<WagerSearchState>,
    onBackClick: (String) -> Unit,
    onEvent: (SearchEvent) -> Unit
) {
    val state by wagerSearchState.collectAsState()
    Scaffold { paddingValues ->
        Column(
            modifier = modifier
                .padding(paddingValues)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(Dimen.MARGIN_MEDIUM)
        ) {
            ExtendedSearchField(
                searchQuery = state.searchText,
                modifier = Modifier.padding(end = Dimen.MARGIN_MEDIUM),
                onValueChange = { onEvent(SearchEvent(it)) },
                onBackButtonClick = { onBackClick(state.searchText) }
            )
            SearchList(wagers = state.wagers)
        }

        BackHandler {
            onBackClick(state.searchText)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ExtendedSearchField(
    modifier: Modifier = Modifier,
    searchQuery: String,
    onValueChange: (String) -> Unit,
    onBackButtonClick: () -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    var textFieldValueState by remember {
        mutableStateOf(
            TextFieldValue(
                text = searchQuery,
                TextRange(searchQuery.length)
            )
        )
    }

    Row(
        horizontalArrangement = Arrangement.spacedBy(Dimen.MARGIN_SMALL),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        IconButton(onClick = onBackButtonClick) {
            Icon(Icons.Filled.ArrowBack, contentDescription = stringResource(id = R.string.text_back_icon))
        }

        OutlinedTextField(
            value = textFieldValueState,
            onValueChange = {
                textFieldValueState = it.copy(selection = TextRange(it.text.length))
                onValueChange(it.text)
            },
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyMedium,
            shape = RoundedCornerShape(Dimen.CORNER_RADIUS_SMALL),
            colors = TextFieldDefaults.outlinedTextFieldColors(containerColor = White),
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
        )

        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun SearchList(modifier: Modifier = Modifier, wagers: Map<String, List<Wager>>) {
    LazyColumn(modifier = modifier) {
        wagers.forEach { (category, wagers) ->
            items(wagers, key = { it.id!! }) {
                if (category == WagerFilter.CLOSED.toString()) {
                    WagerItem(
                        wager = it,
                        Modifier
                            .alpha(ColorValues.ALPHA_HALF)
                            .animateItemPlacement(tween())
                    )
                } else {
                    WagerItem(wager = it, Modifier.animateItemPlacement(tween()))
                }
                Spacer(modifier = Modifier.padding(Dimen.MARGIN_MEDIUM))
            }
        }
    }
}