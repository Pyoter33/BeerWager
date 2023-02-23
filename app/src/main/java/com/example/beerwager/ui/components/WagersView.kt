package com.example.beerwager.ui.components

import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.beerwager.R
import com.example.beerwager.domain.models.WagerFilter
import com.example.beerwager.ui.theme.*
import com.example.beerwager.ui.view_model.WagersViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WagersView(modifier: Modifier = Modifier, viewModel: WagersViewModel = hiltViewModel()) {
    val state by viewModel.notesState.collectAsState()
    Scaffold(
        floatingActionButton = { FloatingButton() }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            SearchField(
                Modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxWidth()
            )
            ChipGroup(
                activeFilters = state.activeFilters,
                modifier = Modifier.padding(horizontal = 8.dp)
            ) { active, filter ->
                if (active) viewModel.onFilter(state.activeFilters - filter) else viewModel.onFilter(
                    state.activeFilters + filter
                )
            }
            WagerList(wagers = state.wagers)
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchField(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val interactionSource = remember { MutableInteractionSource() }

    OutlinedTextField(
        value = "",
        onValueChange = {},
        placeholder = { Text(text = "Search") },
        singleLine = true,
        textStyle = MaterialTheme.typography.bodyMedium,
        leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "") },
        shape = RoundedCornerShape(5.dp),
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
            Toast.makeText(context, "Click", Toast.LENGTH_SHORT).show()
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
    Row(modifier, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
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
        shape = RoundedCornerShape(10.dp),
        containerColor = Green,
        contentColor = Black,
        elevation = FloatingActionButtonDefaults.elevation(8.dp)
    ) {
        Icon(Icons.Outlined.Edit, "")
    }
}

@Preview(showBackground = true)
@Composable
fun Preview2() {
    AppTheme {
        WagersView()
    }
}