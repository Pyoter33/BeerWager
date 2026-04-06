package com.example.beerwager.ui.components.wagerlist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.beerwager.R
import com.example.beerwager.domain.models.WagerCategory
import com.example.beerwager.ui.components.nav.ConfigureTopBar
import com.example.beerwager.ui.components.nav.TopBarAction
import com.example.beerwager.ui.state.FilterEvent
import com.example.beerwager.ui.state.WagersState
import com.example.beerwager.ui.theme.Black
import com.example.beerwager.ui.theme.Green
import com.example.beerwager.ui.theme.White
import com.example.beerwager.utils.Dimen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WagersView(
    modifier: Modifier = Modifier,
    state: WagersState,
    searchQuery: String,
    onSearchClick: (String) -> Unit,
    setSearchQuery: (String) -> Unit,
    onCreateClick: () -> Unit,
    onWagerClick: (Long) -> Unit,
    onEvent: (FilterEvent) -> Unit,
    onLogoutClick: () -> Unit
) {
    ConfigureTopBar(
        title = stringResource(R.string.app_name),
        showBack = false,
        onBack = null,
        actions = listOf(
            TopBarAction(
                icon = Icons.AutoMirrored.Filled.Logout,
                onClick = { onLogoutClick() }
            )
        )
    )

    setSearchQuery(searchQuery)
    Scaffold(
        floatingActionButton = { FloatingButton(onClick = onCreateClick) }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            SearchField(
                modifier = Modifier
                    .padding(horizontal = Dimen.SPACING_XS)
                    .fillMaxWidth(),
                searchQuery = state.searchText,
                onClick = {
                    onSearchClick(state.searchText)
                }
            )
            ChipGroup(
                activeFilters = state.activeFilters,
                modifier = Modifier.padding(horizontal = Dimen.SPACING_XS)
            ) { active, filter ->
                if (active) {
                    onEvent(FilterEvent(state.activeFilters - filter))
                } else {
                    onEvent(FilterEvent(state.activeFilters + filter))
                }
            }
            WagerList(wagers = state.wagers, onWagerClick)
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
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = White,
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
    activeFilters: List<WagerCategory>,
    onFilter: (Boolean, WagerCategory) -> Unit
) {
    Row(modifier, horizontalArrangement = Arrangement.spacedBy(Dimen.SPACING_XS)) {
        FilterChip(
            selected = WagerCategory.CLOSED in activeFilters,
            onClick = { onFilter(WagerCategory.CLOSED in activeFilters, WagerCategory.CLOSED) },
            {
                Text(
                    text = stringResource(id = R.string.text_closed),
                    style = MaterialTheme.typography.bodyMedium
                )
            })

        FilterChip(selected = WagerCategory.UPCOMING in activeFilters, onClick = {
            onFilter(WagerCategory.UPCOMING in activeFilters, WagerCategory.UPCOMING)
        }, {
            Text(
                text = stringResource(id = R.string.text_upcoming),
                style = MaterialTheme.typography.bodyMedium
            )
        })

        FilterChip(selected = WagerCategory.FUTURE in activeFilters, onClick = {
            onFilter(WagerCategory.FUTURE in activeFilters, WagerCategory.FUTURE)
        }, {
            Text(
                text = stringResource(id = R.string.text_future),
                style = MaterialTheme.typography.bodyMedium
            )
        })
    }
}

@Composable
private fun FloatingButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    FloatingActionButton(
        modifier = modifier,
        onClick = onClick,
        shape = RoundedCornerShape(Dimen.CORNER_RADIUS_BIG),
        containerColor = Green,
        contentColor = Black,
        elevation = FloatingActionButtonDefaults.elevation(Dimen.SPACING_XS)
    ) {
        Icon(Icons.Outlined.Edit, stringResource(id = R.string.text_create_icon))
    }
}
