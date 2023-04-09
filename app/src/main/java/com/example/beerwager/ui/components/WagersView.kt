package com.example.beerwager.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.beerwager.R
import com.example.beerwager.domain.models.WagerFilter
import com.example.beerwager.ui.state.FilterEvent
import com.example.beerwager.ui.state.WagersState
import com.example.beerwager.ui.theme.*
import com.example.beerwager.utils.Dimen
import kotlinx.coroutines.flow.StateFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WagersView(
    modifier: Modifier = Modifier,
    wagersState: StateFlow<WagersState>,
    searchQuery: String,
    onSearchClick: (String) -> Unit,
    setSearchQuery: (String) -> Unit,
    onEvent: (FilterEvent) -> Unit
) {
    val state by wagersState.collectAsState()
    setSearchQuery(searchQuery)

    Scaffold(
        floatingActionButton = { FloatingButton() }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            SearchField(
                modifier = Modifier
                    .padding(horizontal = Dimen.MARGIN_MEDIUM)
                    .fillMaxWidth(),
                searchQuery = state.searchText,
                onClick = {
                    onSearchClick(state.searchText)
                }
            )
            ChipGroup(
                activeFilters = state.activeFilters,
                modifier = Modifier.padding(horizontal = Dimen.MARGIN_MEDIUM)
            ) { active, filter ->
                if (active) {
                    onEvent(FilterEvent(state.activeFilters - filter))
                } else {
                    onEvent(FilterEvent(state.activeFilters + filter))
                }
            }
            WagerList(wagers = state.wagers)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchField(modifier: Modifier = Modifier, searchQuery: String, onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }

    OutlinedTextField(
        value = searchQuery,
        onValueChange = {},
        placeholder = { Text(text = "Search") },
        singleLine = true,
        textStyle = MaterialTheme.typography.bodyMedium,
        leadingIcon = { Icon(Icons.Filled.Search, contentDescription = stringResource(id = R.string.text_search_icon)) },
        shape = RoundedCornerShape(Dimen.CORNER_RADIUS_SMALL),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            containerColor = White,
            disabledTextColor = MaterialTheme.colorScheme.onSurface,
            disabledBorderColor = MaterialTheme.colorScheme.outline,
            disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
            disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
            disabledLeadingIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
            disabledTrailingIconColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        enabled = false,
        modifier = modifier.clickable(interactionSource, null) {
            onClick()
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChipGroup(
    modifier: Modifier = Modifier,
    activeFilters: List<WagerFilter>,
    onFilter: (Boolean, WagerFilter) -> Unit
) {
    Row(modifier, horizontalArrangement = Arrangement.spacedBy(Dimen.MARGIN_MEDIUM)) {
        FilterChip(
            selected = WagerFilter.CLOSED in activeFilters,
            onClick = { onFilter(WagerFilter.CLOSED in activeFilters, WagerFilter.CLOSED) },
            {
                Text(
                    text = stringResource(id = R.string.text_closed),
                    style = MaterialTheme.typography.bodyMedium
                )
            })

        FilterChip(selected = WagerFilter.UPCOMING in activeFilters, onClick = {
            onFilter(WagerFilter.UPCOMING in activeFilters, WagerFilter.UPCOMING)
        }, {
            Text(
                text = stringResource(id = R.string.text_upcoming),
                style = MaterialTheme.typography.bodyMedium
            )
        })

        FilterChip(selected = WagerFilter.FUTURE in activeFilters, onClick = {
            onFilter(WagerFilter.FUTURE in activeFilters, WagerFilter.FUTURE)
        }, {
            Text(
                text = stringResource(id = R.string.text_future),
                style = MaterialTheme.typography.bodyMedium
            )
        })
    }
}

@Composable
private fun FloatingButton(modifier: Modifier = Modifier) {
    FloatingActionButton(
        modifier = modifier,
        onClick = { },
        shape = RoundedCornerShape(Dimen.CORNER_RADIUS_BIG),
        containerColor = Green,
        contentColor = Black,
        elevation = FloatingActionButtonDefaults.elevation(Dimen.MARGIN_MEDIUM)
    ) {
        Icon(Icons.Outlined.Edit, "")
    }
}
