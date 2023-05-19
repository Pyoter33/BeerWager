package com.example.beerwager.ui.components

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.text.format.DateFormat
import android.widget.DatePicker
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.AddCircleOutline
import androidx.compose.material.icons.outlined.RemoveCircleOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.example.beerwager.R
import com.example.beerwager.domain.models.WagerFilter
import com.example.beerwager.ui.theme.*
import com.example.beerwager.utils.ColorValues.ALPHA_DISABLED
import com.example.beerwager.utils.ColorValues.ALPHA_SMALL
import com.example.beerwager.utils.Dimen
import com.example.beerwager.utils.visible
import java.time.LocalDate
import java.time.LocalTime
import java.util.*

@Composable
fun TopView(
    isBlocked: Boolean,
    category: String,
    onBackClick: () -> Unit,
    onCloseClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = modifier.fillMaxWidth()) {
        IconButton(onClick = onBackClick) {
            Icon(
                Icons.Filled.ArrowBack,
                contentDescription = stringResource(id = R.string.text_back_icon)
            )
        }
        if (isBlocked) {
            Row(horizontalArrangement = Arrangement.End) {
                if (category == WagerFilter.UPCOMING.toString()) {
                    IconButton(onClick = onCloseClick) {
                        Icon(
                            Icons.Filled.Done,
                            contentDescription = stringResource(id = R.string.text_done_icon)
                        )
                    }
                }
                if (category != WagerFilter.CLOSED.toString()) {
                    IconButton(onClick = onEditClick) {
                        Icon(
                            Icons.Filled.Edit,
                            contentDescription = stringResource(id = R.string.text_edit_icon)
                        )
                    }
                }
                IconButton(onClick = onDeleteClick) {
                    Icon(
                        Icons.Filled.Delete,
                        contentDescription = stringResource(id = R.string.text_delete_icon)
                    )
                }
            }
        }
    }
}

@Composable
fun BeersAtStakeView(
    beersAtStake: Int,
    isBlocked: Boolean,
    onBeersChanged: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        if (!isBlocked) {
            IconButton(onClick = {
                onBeersChanged(beersAtStake - 1)
            }) {
                Icon(Icons.Outlined.RemoveCircleOutline, "")
            }
        }
        Text(
            text = beersAtStake.toString(),
            style = MaterialTheme.typography.titleLarge,
            color = Black
        )
        Icon(Icons.Default.SportsBar, "", modifier = Modifier.size(Dimen.ICON_SIZE_XXLARGE))
        if (!isBlocked) {
            IconButton(onClick = {
                onBeersChanged(beersAtStake + 1)
            }) {
                Icon(Icons.Outlined.AddCircleOutline, "")
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShortTextView(
    label: String,
    text: String,
    isBlocked: Boolean,
    onTextChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
    errorMessage: String? = null,
    placeholder: String? = null
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = Black,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(bottom = Dimen.MARGIN_SMALL)
        )
        OutlinedTextField(
            value = text,
            onValueChange = onTextChanged,
            singleLine = true,
            enabled = !isBlocked,
            placeholder = {
                Text(text = placeholder.orEmpty(), style = MaterialTheme.typography.bodyMedium)
            },
            isError = !errorMessage.isNullOrEmpty(),
            textStyle = MaterialTheme.typography.bodyMedium,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                textColor = Black,
                focusedBorderColor = Black,
                unfocusedBorderColor = Black,
                disabledBorderColor = Black.copy(alpha = ALPHA_DISABLED)
            ),
            modifier = Modifier
                .padding(bottom = Dimen.MARGIN_SMALL)
                .fillMaxWidth()
        )
        ErrorTextView(errorMessage = errorMessage)
    }
}

@Composable
fun LongTextView(
    label: String,
    text: String,
    isBlocked: Boolean,
    onTextChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
    errorMessage: String? = null,
    placeholder: String? = null
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = Black,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(bottom = Dimen.MARGIN_SMALL)
        )

        val shape = RoundedCornerShape(Dimen.CORNER_RADIUS_SMALL)
        var isFocused by remember { mutableStateOf(false) }
        BasicTextField(
            value = text,
            onValueChange = onTextChanged,
            enabled = !isBlocked,
            textStyle = MaterialTheme.typography.bodyMedium.copy(color = Black.copy(alpha = if (isBlocked) ALPHA_DISABLED else DefaultAlpha)),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            modifier = Modifier
                .height(Dimen.LONG_TEXT_FIELD_HEIGHT)
                .padding(bottom = Dimen.MARGIN_SMALL)
                .onFocusChanged {
                    isFocused = it.isFocused
                }
                .fillMaxWidth()
        ) { textField ->
            val boxColour = when {
                isBlocked -> Black.copy(alpha = ALPHA_DISABLED)
                errorMessage.isNullOrEmpty() -> Black
                else -> ErrorRed
            }
            Box(
                Modifier
                    .fillMaxWidth()
                    .clip(shape)
                    .border(
                        BorderStroke(
                            if (isFocused) Dimen.BORDER_FOCUSED else Dimen.BORDER_UNFOCUSED,
                            boxColour
                        ), shape
                    )
                    .padding(vertical = Dimen.MARGIN_SMALL, horizontal = Dimen.MARGIN_MEDIUM)

            ) {
                if (text.isEmpty() && !placeholder.isNullOrEmpty()) {
                    Text(
                        text = placeholder,
                        color = Grey,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                textField()
            }
        }
    }
    ErrorTextView(errorMessage = errorMessage)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DatePickerView(
    text: String,
    isBlocked: Boolean,
    onDateChanged: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
    errorMessage: String? = null,
    placeholder: String? = null
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val year = calendar[Calendar.YEAR]
    val month = calendar[Calendar.MONTH]
    val dayOfMonth = calendar[Calendar.DAY_OF_MONTH]

    val datePicker = DatePickerDialog(
        context,
        { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDayOfMonth: Int ->
            onDateChanged(LocalDate.of(selectedYear, selectedMonth + 1, selectedDayOfMonth))
        }, year, month, dayOfMonth
    )

    OutlinedTextField(
        value = text,
        onValueChange = {},
        enabled = false,
        singleLine = true,
        placeholder = {
            Text(text = placeholder.orEmpty(), style = MaterialTheme.typography.bodyMedium)
        },
        isError = !errorMessage.isNullOrEmpty(),
        textStyle = MaterialTheme.typography.bodyMedium,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            textColor = Black,
            focusedBorderColor = Black,
            unfocusedBorderColor = Black,
            disabledBorderColor = if (isBlocked) Black.copy(alpha = ALPHA_DISABLED) else Black,
            disabledTextColor = if (isBlocked) Black.copy(alpha = ALPHA_DISABLED) else Black
            ),
        leadingIcon = {
            Icon(
                Icons.Default.CalendarMonth,
                stringResource(id = R.string.text_calendar_icon),
                tint = if (errorMessage.isNullOrEmpty()) Black.copy(alpha = if (isBlocked) ALPHA_DISABLED else DefaultAlpha) else ErrorRed
            )
        },
        modifier = modifier
            .padding(bottom = Dimen.MARGIN_SMALL)
            .width(Dimen.SMALL_TEXT_FIELD_WIDTH)
            .clickable(enabled = !isBlocked) {
                datePicker.show()
            }
    )
}

@Composable
private fun SwitchView(
    label: String,
    isChecked: Boolean,
    isBlocked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier.fillMaxWidth()) {
        Switch(
            checked = isChecked,
            enabled = !isBlocked,
            onCheckedChange = { onCheckedChange(it) },
            colors = SwitchDefaults.colors(
                checkedBorderColor = Black,
                checkedTrackColor = Green,
                uncheckedTrackColor = Green.copy(alpha = ALPHA_SMALL)
            ),
            modifier = Modifier
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = Black,
            modifier = Modifier.padding(start = Dimen.MARGIN_MEDIUM)
        )
    }
}

@Composable
fun DatePickerWithSwitchView(
    dateLabel: String,
    switchLabel: String,
    dateText: String,
    isChecked: Boolean,
    isBlocked: Boolean,
    onDateChanged: (LocalDate) -> Unit,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    errorMessage: String? = null,
    placeholder: String? = null
) {
    Column(modifier = modifier) {
        Text(
            text = dateLabel,
            style = MaterialTheme.typography.bodyMedium,
            color = Black,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(bottom = Dimen.MARGIN_SMALL)
        )
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            DatePickerView(
                text = dateText,
                placeholder = placeholder,
                isBlocked = isBlocked,
                errorMessage = errorMessage,
                onDateChanged = onDateChanged
            )
            SwitchView(
                label = switchLabel,
                isChecked = isChecked,
                isBlocked = isBlocked,
                onCheckedChange = onCheckedChange,
                modifier = Modifier.padding(start = Dimen.SWITCH_MARGIN)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerView(
    label: String,
    text: String,
    isBlocked: Boolean,
    onTimeChanged: (LocalTime) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val hour = calendar[Calendar.HOUR_OF_DAY]
    val minute = calendar[Calendar.MINUTE]

    val timePicker = TimePickerDialog(
        context,
        { _, selectedHour: Int, selectedMinute: Int ->
            onTimeChanged(LocalTime.of(selectedHour, selectedMinute))
        }, hour, minute, DateFormat.is24HourFormat(context)
    )

    Column(modifier = modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = Black,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(bottom = Dimen.MARGIN_SMALL)
        )

        OutlinedTextField(
            value = text,
            onValueChange = {},
            enabled = false,
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyMedium,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                textColor = Black,
                focusedBorderColor = Black,
                unfocusedBorderColor = Black,
                disabledBorderColor = if (isBlocked) Black.copy(alpha = ALPHA_DISABLED) else Black,
                disabledTextColor = if (isBlocked) Black.copy(alpha = ALPHA_DISABLED) else Black,
            ),
            leadingIcon = {
                Icon(
                    Icons.Default.Schedule,
                    stringResource(id = R.string.text_schedule_icon),
                    tint = Black.copy(alpha = if (isBlocked) ALPHA_DISABLED else DefaultAlpha)
                )
            },
            modifier = Modifier
                .padding(bottom = Dimen.MARGIN_SMALL)
                .width(Dimen.SMALL_TEXT_FIELD_WIDTH)
                .clickable(enabled = !isBlocked) {
                    timePicker.show()
                }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckBoxView(
    label: String,
    isChecked: Boolean,
    isBlocked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier) {
        CompositionLocalProvider(LocalMinimumTouchTargetEnforcement provides false) {
            Checkbox(
                checked = isChecked,
                enabled = !isBlocked,
                onCheckedChange = onCheckedChange,
                colors = CheckboxDefaults.colors(checkmarkColor = White)
            )
        }
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = Black,
            modifier = Modifier.padding(start = Dimen.MARGIN_BIG)
        )
    }
}

@Composable
fun ColourPickerView(
    label: String,
    chosenColour: Int,
    colourList: List<Color>,
    isBlocked: Boolean,
    onColourChanged: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = Black,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(bottom = Dimen.MARGIN_MEDIUM)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            colourList.forEachIndexed { index, colour ->
                Box(
                    modifier = Modifier
                        .size(Dimen.COLOUR_BOX_SIZE)
                        .background(
                            shape = RoundedCornerShape(Dimen.CORNER_RADIUS_XSMALL),
                            color = if (index == chosenColour) colour else colour.copy(alpha = ALPHA_SMALL)
                        )
                        .clickable(enabled = !isBlocked) {
                            onColourChanged(index)
                        }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WagerersSectionsView(
    label: String,
    wagererName: String,
    isBlocked: Boolean,
    onWagererNameChange: (String) -> Unit,
    onWagererAdded: () -> Unit,
    modifier: Modifier = Modifier,
    errorMessage: String?
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = Black,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(bottom = Dimen.MARGIN_SMALL)
        )
        OutlinedTextField(
            value = wagererName,
            onValueChange = onWagererNameChange,
            singleLine = true,
            enabled = !isBlocked,
            isError = !errorMessage.isNullOrEmpty(),
            textStyle = MaterialTheme.typography.bodyMedium,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                textColor = Black,
                focusedBorderColor = Black,
                unfocusedBorderColor = Black,
                disabledBorderColor = Black.copy(alpha = ALPHA_DISABLED)
            ),
            trailingIcon = {
                Icon(
                    Icons.Default.AddCircle,
                    contentDescription = stringResource(id = R.string.text_add_circle_icon),
                    tint = if (isBlocked) Black.copy(alpha = ALPHA_DISABLED) else Green,
                    modifier = Modifier
                        .size(Dimen.ICON_SIZE_BIG)
                        .clickable(enabled = !isBlocked) {
                            onWagererAdded()
                        }
                )
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun ErrorTextView(errorMessage: String?, modifier: Modifier = Modifier) {
    Text(
        text = errorMessage.orEmpty(),
        style = MaterialTheme.typography.bodyMedium,
        color = ErrorRed,
        modifier = modifier.alpha(if (errorMessage.isNullOrEmpty()) 0f else 1f)
    )
}

@Composable
fun WagererItem(
    index: Int,
    value: String,
    isBlocked: Boolean,
    onWagererRemoved: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = Black,
        )
        IconButton(onClick = { onWagererRemoved(index) }, modifier = Modifier.visible(!isBlocked)) {
            Icon(
                Icons.Default.Delete,
                contentDescription = stringResource(id = R.string.text_delete_icon)
            )
        }
    }
}

@Composable
fun CreateButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        shape = RoundedCornerShape(
            topStart = Dimen.CORNER_RADIUS_BIG,
            topEnd = Dimen.CORNER_RADIUS_BIG
        ),
        onClick = onClick,
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium,
            color = Black,
            modifier = Modifier.padding(Dimen.MARGIN_SMALL)
        )
    }
}
