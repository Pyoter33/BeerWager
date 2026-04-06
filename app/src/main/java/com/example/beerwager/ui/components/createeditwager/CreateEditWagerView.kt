package com.example.beerwager.ui.components.createeditwager

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.example.beerwager.R
import com.example.beerwager.data.data_source.Wager
import com.example.beerwager.domain.models.WagerCategory
import com.example.beerwager.ui.components.WagerYesCancelDialog
import com.example.beerwager.ui.components.nav.ConfigureTopBar
import com.example.beerwager.ui.components.nav.TopBarAction
import com.example.beerwager.ui.state.AddWagererEvent
import com.example.beerwager.ui.state.AllDayChangedEvent
import com.example.beerwager.ui.state.BeersChangedEvent
import com.example.beerwager.ui.state.CalendarChangedEvent
import com.example.beerwager.ui.state.CloseWagerEvent
import com.example.beerwager.ui.state.ColourChangedEvent
import com.example.beerwager.ui.state.CreateWagerState
import com.example.beerwager.ui.state.CreateWagersEvent
import com.example.beerwager.ui.state.DateChangedEvent
import com.example.beerwager.ui.state.DeleteWagerEvent
import com.example.beerwager.ui.state.DescriptionChangedEvent
import com.example.beerwager.ui.state.EditUnlockedEvent
import com.example.beerwager.ui.state.NotificationChangedEvent
import com.example.beerwager.ui.state.RemoveWagererEvent
import com.example.beerwager.ui.state.SubmitWagerEvent
import com.example.beerwager.ui.state.TimeChangedEvent
import com.example.beerwager.ui.state.TitleChangedEvent
import com.example.beerwager.ui.state.WagererNameChangedEvent
import com.example.beerwager.utils.Dimen
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun CreateEditWagerView(
    modifier: Modifier = Modifier,
    state: CreateWagerState,
    onEvent: (CreateWagersEvent) -> Unit,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val dateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
    val timeFormatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)
    var showBackDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showCloseDialog by remember { mutableStateOf(false) }
    val permissionStateCalendar = rememberPermissionState(android.Manifest.permission.WRITE_CALENDAR)
    val backButtonHandler = { if (!state.isBlocked) showBackDialog = true else onBackClick() }
    val actions = listOf(
        TopBarAction(
            icon = Icons.Filled.Done,
            onClick = { showCloseDialog = true },
            visible = state.isBlocked && state.wagerCategory == WagerCategory.UPCOMING
        ),
        TopBarAction(
            icon = Icons.Filled.Edit,
            onClick = { onEvent(EditUnlockedEvent) },
            visible = state.isBlocked && state.wagerCategory != WagerCategory.CLOSED
        ),
        TopBarAction(
            icon = Icons.Filled.Delete,
            onClick = { showDeleteDialog = true },
            visible = state.isBlocked
        )
    )

    ConfigureTopBar(
        title = stringResource(R.string.screen_details),
        showBack = true,
        onBack = { backButtonHandler() },
        actions = actions
    )

    BackHandler { backButtonHandler() }

    if (showDeleteDialog) {
        WagerYesCancelDialog(
            title = stringResource(id = R.string.text_delete_title),
            description = stringResource(id = R.string.text_undo_description),
            onPositive = {
                onEvent(DeleteWagerEvent)
            },
            onDismiss = {
                showDeleteDialog = false
            }
        )
    }

    if (showCloseDialog) {
        WagerYesCancelDialog(
            title = stringResource(id = R.string.text_close_title),
            description = stringResource(id = R.string.text_undo_description),
            onPositive = {
                onEvent(CloseWagerEvent)
            },
            onDismiss = {
                showCloseDialog = false
            }
        )
    }

    if (showBackDialog) {
        WagerYesCancelDialog(
            title = stringResource(id = R.string.text_go_back_title),
            description = stringResource(id = R.string.text_go_back_description),
            onPositive = onBackClick,
            onDismiss = {
                showBackDialog = false
            }
        )
    }

    Scaffold(
        bottomBar = {
            if (!state.isBlocked) {
                CreateButton(
                    label = stringResource(id = R.string.text_submit),
                    onClick = { onEvent(SubmitWagerEvent) },
                )
            }
        }
    ) { paddingValues ->
        VerticalScrollLayout(
            modifier = modifier.padding(bottom = Dimen.SPACING_M),
            contentPadding = paddingValues,
            verticalArrangement = Arrangement.spacedBy(Dimen.SPACING_XXS),
            ChildLayout { _, _ ->
                Box(Modifier.fillMaxWidth()) {
                    BeersAtStakeView(
                        beersAtStake = state.beersAtStake,
                        state.isBlocked,
                        onBeersChanged = { onEvent(BeersChangedEvent(it)) },
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(bottom = Dimen.SPACING_M)
                    )
                }
            },
            ChildLayout { _, _ ->
                ShortTextView(
                    label = stringResource(id = R.string.text_title),
                    text = state.title,
                    state.isBlocked,
                    onTextChanged = { onEvent(TitleChangedEvent(it)) },
                    errorMessage = state.titleError?.let { stringResource(id = it) },
                    modifier = Modifier.padding(
                        start = Dimen.SPACING_M,
                        end = Dimen.SPACING_M
                    )
                )
            },
            ChildLayout { _, _ ->
                LongTextView(
                    label = stringResource(id = R.string.text_description),
                    text = state.description,
                    state.isBlocked,
                    onTextChanged = { onEvent(DescriptionChangedEvent(it)) },
                    errorMessage = state.descriptionError?.let { stringResource(id = it) },
                    modifier = Modifier.padding(
                        start = Dimen.SPACING_M,
                        end = Dimen.SPACING_M
                    )
                )
            },
            ChildLayout { _, _ ->
                DatePickerWithSwitchView(
                    dateLabel = stringResource(id = R.string.text_date),
                    switchLabel = stringResource(id = R.string.text_all_day),
                    dateText = state.date.format(dateFormatter),
                    isChecked = state.time == null,
                    state.isBlocked,
                    onDateChanged = { onEvent(DateChangedEvent(it)) },
                    onCheckedChange = { onEvent(AllDayChangedEvent(it)) },
                    modifier = Modifier.padding(
                        bottom = Dimen.SPACING_L,
                        start = Dimen.SPACING_M,
                        end = Dimen.SPACING_M
                    )
                )
            },
            ChildLayout { _, _ ->
                state.time?.let {
                    TimePickerView(
                        label = stringResource(id = R.string.text_time),
                        text = state.time.format(timeFormatter),
                        state.isBlocked,
                        onTimeChanged = { onEvent(TimeChangedEvent(it)) },
                        modifier = Modifier.padding(
                            bottom = Dimen.SPACING_L,
                            start = Dimen.SPACING_M,
                            end = Dimen.SPACING_M
                        )
                    )
                }
            },
            ChildLayout { _, _ ->
                CheckBoxView(
                    label = stringResource(id = R.string.text_send_notifications),
                    isChecked = state.hasNotification,
                    state.isBlocked,
                    onCheckedChange = {
                        onEvent(NotificationChangedEvent(it))
                    },
                    modifier = Modifier.padding(
                        bottom = Dimen.SPACING_L,
                        start = Dimen.SPACING_M,
                        end = Dimen.SPACING_M
                    )
                )
            },
            ChildLayout { _, _ ->
                CheckBoxView(
                    label = stringResource(id = R.string.text_add_to_calendar),
                    isChecked = state.isInCalendar,
                    state.isBlocked,
                    onCheckedChange = {
                        when {
                            permissionStateCalendar.status.isGranted -> {
                                onEvent(CalendarChangedEvent(it))
                            }
                            permissionStateCalendar.status.shouldShowRationale -> {
                                Toast.makeText(context, R.string.text_accept_permission_calendar, Toast.LENGTH_SHORT).show()
                                permissionStateCalendar.launchPermissionRequest()
                            }
                            else -> {
                                permissionStateCalendar.launchPermissionRequest()
                            }
                        }
                    },
                    modifier = Modifier.padding(
                        bottom = Dimen.SPACING_L,
                        start = Dimen.SPACING_M,
                        end = Dimen.SPACING_M
                    )
                )
            },
            ChildLayout { _, _ ->
                ColourPickerView(
                    label = stringResource(id = R.string.text_colour),
                    chosenColour = state.colour,
                    colourList = Wager.WAGER_COLORS,
                    state.isBlocked,
                    onColourChanged = { onEvent(ColourChangedEvent(it)) },
                    modifier = Modifier.padding(
                        bottom = Dimen.SPACING_L,
                        start = Dimen.SPACING_M,
                        end = Dimen.SPACING_M
                    )
                )
            },
            ChildLayout { _, _ ->
                WagerersSectionsView(
                    label = stringResource(id = R.string.text_wagerers),
                    wagererName = state.wagererName,
                    state.isBlocked,
                    onWagererNameChange = { onEvent(WagererNameChangedEvent(it)) },
                    onWagererAdded = { onEvent(AddWagererEvent) },
                    errorMessage = state.wagerersError?.let { stringResource(id = it) },
                    modifier = Modifier.padding(
                        start = Dimen.SPACING_M,
                        end = Dimen.SPACING_M
                    )
                )
            },
            ChildLayout(
                items = state.wagerers,
            ) { index, item ->
                WagererItem(
                    index = index!!,
                    value = item!! as String,
                    state.isBlocked,
                    onWagererRemoved = { onEvent(RemoveWagererEvent(index)) },
                    modifier = Modifier
                        .padding(start = Dimen.SPACING_M, end = Dimen.SPACING_M)
                        .fillMaxWidth()
                )
            },
            ChildLayout { _, _ ->
                ErrorTextView(
                    errorMessage = state.wagerersError?.let { stringResource(id = it) },
                    modifier = Modifier.padding(
                        start = Dimen.SPACING_M,
                        end = Dimen.SPACING_M
                    )
                )
            }
        )
    }
}

