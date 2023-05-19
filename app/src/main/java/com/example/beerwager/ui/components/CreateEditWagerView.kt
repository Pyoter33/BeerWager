package com.example.beerwager.ui.components

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.example.beerwager.R
import com.example.beerwager.data.data_source.Wager
import com.example.beerwager.ui.state.*
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
    val permissionState = rememberPermissionState(android.Manifest.permission.WRITE_CALENDAR)


    BackHandler { if (!state.isBlocked) showBackDialog = true else onBackClick() }

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
            modifier = modifier.padding(bottom = Dimen.MARGIN_LARGE),
            contentPadding = paddingValues,
            verticalArrangement = Arrangement.spacedBy(Dimen.MARGIN_SMALL),
            ChildLayout { _, _ ->
                TopView(
                    state.isBlocked,
                    state.category,
                    onCloseClick = {
                        showCloseDialog = true
                    },
                    onBackClick = {
                        if (!state.isBlocked) {
                            showBackDialog = true
                        } else {
                            onBackClick()
                        }
                    },
                    onEditClick = { onEvent(EditUnlockedEvent) },
                    onDeleteClick = { showDeleteDialog = true }
                )
            },
            ChildLayout { _, _ ->
                Box(Modifier.fillMaxWidth()) {
                    BeersAtStakeView(
                        beersAtStake = state.beersAtStake,
                        state.isBlocked,
                        onBeersChanged = { onEvent(BeersChangedEvent(it)) },
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(bottom = Dimen.MARGIN_LARGE)
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
                        start = Dimen.MARGIN_LARGE,
                        end = Dimen.MARGIN_LARGE
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
                        start = Dimen.MARGIN_LARGE,
                        end = Dimen.MARGIN_LARGE
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
                        bottom = Dimen.MARGIN_XLARGE,
                        start = Dimen.MARGIN_LARGE,
                        end = Dimen.MARGIN_LARGE
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
                            bottom = Dimen.MARGIN_XLARGE,
                            start = Dimen.MARGIN_LARGE,
                            end = Dimen.MARGIN_LARGE
                        )
                    )
                }
            },
            ChildLayout { _, _ ->
                CheckBoxView(
                    label = stringResource(id = R.string.text_send_notifications),
                    isChecked = state.hasNotification,
                    state.isBlocked,
                    onCheckedChange = { onEvent(NotificationChangedEvent(it)) },
                    modifier = Modifier.padding(
                        bottom = Dimen.MARGIN_XLARGE,
                        start = Dimen.MARGIN_LARGE,
                        end = Dimen.MARGIN_LARGE
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
                            permissionState.status.isGranted -> {
                                onEvent(CalendarChangedEvent(it))
                            }
                            permissionState.status.shouldShowRationale -> {
                                Toast.makeText(context, R.string.text_accept_permission, Toast.LENGTH_SHORT).show()
                                permissionState.launchPermissionRequest()
                            }
                            else -> {
                                permissionState.launchPermissionRequest()
                            }
                        }
                    },
                    modifier = Modifier.padding(
                        bottom = Dimen.MARGIN_XLARGE,
                        start = Dimen.MARGIN_LARGE,
                        end = Dimen.MARGIN_LARGE
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
                        bottom = Dimen.MARGIN_XLARGE,
                        start = Dimen.MARGIN_LARGE,
                        end = Dimen.MARGIN_LARGE
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
                        start = Dimen.MARGIN_LARGE,
                        end = Dimen.MARGIN_LARGE
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
                        .padding(start = Dimen.MARGIN_LARGE, end = Dimen.MARGIN_LARGE)
                        .fillMaxWidth()
                )
            },
            ChildLayout { _, _ ->
                ErrorTextView(
                    errorMessage = state.wagerersError?.let { stringResource(id = it) },
                    modifier = Modifier.padding(
                        start = Dimen.MARGIN_LARGE,
                        end = Dimen.MARGIN_LARGE
                    )
                )
            }
        )
    }
}

