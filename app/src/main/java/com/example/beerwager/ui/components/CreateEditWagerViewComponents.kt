package com.example.beerwager.ui.components

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.widget.DatePicker
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.AddCircleOutline
import androidx.compose.material.icons.outlined.RemoveCircleOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.beerwager.data.data_source.Wager
import com.example.beerwager.ui.theme.*
import com.example.beerwager.utils.Dimen
import com.example.beerwager.utils.Dimen.ICON_SIZE_BIG
import com.example.beerwager.utils.Dimen.MARGIN_MEDIUM
import com.example.beerwager.utils.Dimen.MARGIN_SMALL
import java.time.LocalDate
import java.time.LocalTime
import java.util.*

@Composable
fun BeersAtStakeView(
    beersAtStake: Int,
    onBeersChanged: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier, verticalAlignment = CenterVertically) {
        IconButton(onClick = {
            onBeersChanged(beersAtStake - 1)
        }) {
            Icon(Icons.Outlined.RemoveCircleOutline, "")
        }
        Text(
            text = beersAtStake.toString(),
            style = MaterialTheme.typography.titleLarge,
            color = Black
        )
        Icon(Icons.Default.SportsBar, "", modifier = Modifier.size(ICON_SIZE_BIG))
        IconButton(onClick = {
            onBeersChanged(beersAtStake + 1)
        }) {
            Icon(Icons.Outlined.AddCircleOutline, "")
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShortTextView(
    label: String,
    text: String,
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
                .padding(bottom = MARGIN_SMALL)
        )
        OutlinedTextField(
            value = text,
            onValueChange = onTextChanged,
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
                disabledBorderColor = Black
            ),
            modifier = Modifier
                .padding(bottom = MARGIN_SMALL)
                .fillMaxWidth()
        )
        Text(
            text = errorMessage.orEmpty(),
            style = MaterialTheme.typography.bodyMedium,
            color = ErrorRed,
            modifier = Modifier.alpha(if (errorMessage.isNullOrEmpty()) 0f else 1f)
        )
    }
}

@Composable
fun LongTextView(
    label: String,
    text: String,
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
                .padding(bottom = MARGIN_SMALL)
        )

        val shape = RoundedCornerShape(Dimen.CORNER_RADIUS_SMALL)
        var isFocused by remember { mutableStateOf(false) }
        BasicTextField(
            value = text,
            onValueChange = onTextChanged,
            textStyle = MaterialTheme.typography.bodyMedium,
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            modifier = Modifier
                .height(100.dp)
                .padding(bottom = MARGIN_SMALL)
                .onFocusChanged {
                    isFocused = it.isFocused
                }
                .fillMaxWidth()
        ) { textField ->
            val boxColour = if (errorMessage.isNullOrEmpty()) Black else ErrorRed
            Box(
                Modifier
                    .fillMaxWidth()
                    .clip(shape)
                    .border(BorderStroke(if (isFocused) 2.dp else 1.dp, boxColour), shape)
                    .padding(vertical = MARGIN_SMALL, horizontal = MARGIN_MEDIUM)

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
        Text(
            text = errorMessage.orEmpty(),
            style = MaterialTheme.typography.bodyMedium,
            color = ErrorRed,
            modifier = Modifier.alpha(if (errorMessage.isNullOrEmpty()) 0f else 1f)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DatePickerView(
    text: String,
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
        readOnly = true,
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
            disabledBorderColor = Black
        ),
        leadingIcon = {
            Icon(
                Icons.Default.CalendarMonth,
                "",
                tint = if (errorMessage.isNullOrEmpty()) Black else ErrorRed
            )
        },
        modifier = modifier
            .padding(bottom = MARGIN_SMALL)
            .width(180.dp)
            .clickable {
                datePicker.show()
            }
    )

}

@Composable
private fun SwitchView(
    label: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(verticalAlignment = CenterVertically, modifier = modifier.fillMaxWidth()) {
        Switch(
            checked = isChecked,
            onCheckedChange = { onCheckedChange(it) },
            colors = SwitchDefaults.colors(
                checkedBorderColor = Black,
                checkedTrackColor = Green,
                uncheckedTrackColor = Green.copy(alpha = 0.3f)
            ),
            modifier = Modifier
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = Black,
            modifier = Modifier.padding(start = MARGIN_MEDIUM)
        )
    }
}

@Composable
fun DatePickerWithSwitchView(
    dateLabel: String,
    switchLabel: String,
    dateText: String,
    isChecked: Boolean,
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
                .padding(bottom = MARGIN_SMALL)
        )

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            DatePickerView(
                text = dateText,
                placeholder = placeholder,
                errorMessage = errorMessage,
                onDateChanged = onDateChanged
            )
            SwitchView(
                label = switchLabel,
                isChecked = isChecked,
                onCheckedChange = onCheckedChange,
                modifier = Modifier.padding(start = 30.dp)
            )
        }
        Text(
            text = errorMessage.orEmpty(),
            style = MaterialTheme.typography.bodyMedium,
            color = ErrorRed,
            modifier = Modifier.alpha(if (errorMessage.isNullOrEmpty()) 0f else 1f)
        )
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerView(
    label: String,
    text: String,
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
        }, hour, minute, false
    )

    Column(modifier = modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = Black,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(bottom = MARGIN_SMALL)
        )

        OutlinedTextField(
            value = text,
            onValueChange = {},
            readOnly = true,
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyMedium,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                textColor = Black,
                focusedBorderColor = Black,
                unfocusedBorderColor = Black,
                disabledBorderColor = Black
            ),
            leadingIcon = {
                Icon(Icons.Default.Schedule, "", tint = Black)
            },
            modifier = Modifier
                .padding(bottom = MARGIN_SMALL)
                .width(180.dp)
                .clickable {
                    timePicker.show()
                }
        )
    }
}

@Composable
fun CheckBoxView(
    label: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(verticalAlignment = CenterVertically, modifier = modifier) {
        Checkbox(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(
                checkmarkColor = White
            )
        )

        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = Black,
            modifier = Modifier
                .padding(start = MARGIN_MEDIUM)
        )
    }
}

@Composable
fun ColourPickerView(
    label: String,
    chosenColour: Int,
    colourList: List<Color>,
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
                .padding(bottom = MARGIN_MEDIUM)
        )

        Row(verticalAlignment = CenterVertically, horizontalArrangement = Arrangement.Start, modifier = Modifier.fillMaxWidth()) {
            colourList.forEachIndexed { index, colour ->
                Box(
                    modifier = Modifier
                        .padding(end = 30.dp)
                        .size(32.dp)
                        .background(
                            shape = RoundedCornerShape(2.dp),
                            color = if (index == chosenColour) colour else colour.copy(alpha = 0.3f)
                        )
                        .clickable {
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
    wagerers: List<String>,
    onWagererNameChange: (String) -> Unit,
    onWagererAdded: () -> Unit,
    onWagererRemoved: (Int) -> Unit,
    errorMessage: String?, 
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = Black,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(bottom = MARGIN_SMALL)
        )
        Row {
            Column(modifier = Modifier.weight(5f)) {
                OutlinedTextField(
                    value = wagererName,
                    onValueChange = onWagererNameChange,
                    singleLine = true,
                    isError = !errorMessage.isNullOrEmpty(),
                    textStyle = MaterialTheme.typography.bodyMedium,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = Black,
                        focusedBorderColor = Black,
                        unfocusedBorderColor = Black,
                        disabledBorderColor = Black
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                )
                LazyColumn(userScrollEnabled = false, modifier = Modifier.padding(start = 5.dp, bottom = 5.dp)) {
                    itemsIndexed(wagerers) { i, value ->
                        Row(horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = value,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Black,
                            )
                            IconButton(onClick = {
                                onWagererRemoved(i)
                            }) {
                                Icon(Icons.Default.Delete, contentDescription = "")
                            }
                        }
                    }
                }
            }
            IconButton(onClick = onWagererAdded, modifier = Modifier
                .weight(1f)
                .padding(top = 4.dp)) {
                Icon(Icons.Default.AddCircle, contentDescription = "", tint = Green, modifier = Modifier.size(32.dp))
            }
        }
        Text(
            text = errorMessage.orEmpty(),
            style = MaterialTheme.typography.bodyMedium,
            color = ErrorRed,
            modifier = Modifier.alpha(if (errorMessage.isNullOrEmpty()) 0f else 1f)
        )
    }
}

@Composable
fun CreateButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp), onClick = onClick) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = Black
        )
    }
}

@Preview(showBackground = true)
@Composable
fun Test() {
    AppTheme {
        Column(Modifier.fillMaxWidth()) {
            ShortTextView(
                label = "This is label",
                text = "",
                onTextChanged = {},
                errorMessage = null,
                placeholder = "This is placeholder",
                modifier = Modifier.padding(5.dp)
            )

            LongTextView(
                label = "This is label",
                text = "",
                onTextChanged = {},
                errorMessage = null,
                placeholder = "This is placeholder",
                modifier = Modifier.padding(5.dp)
            )

            DatePickerWithSwitchView(
                dateLabel = "Date",
                switchLabel = "All day",
                dateText = "20.10.2021",
                isChecked = true,
                onDateChanged = {},
                onCheckedChange = {},
                errorMessage = "Error message long message",
                modifier = Modifier.padding(5.dp)
            )

            TimePickerView(
                label = "Time",
                text = "12:00",
                onTimeChanged = {},
                modifier = Modifier.padding(5.dp)
            )

            CheckBoxView(
                label = "Send notifications",
                isChecked = true,
                onCheckedChange = {}
            )

            ColourPickerView(
                label = "Colours",
                chosenColour = 0,
                colourList = Wager.WAGER_COLORS,
                onColourChanged = {},
                modifier = Modifier.padding(5.dp)
            )

            WagerersSectionsView(
                label = "Wagerers",
                wagererName = "Danny Bou",
                wagerers = listOf("John Doe", "Michael Jennings"),
                onWagererNameChange = {},
                onWagererAdded = {},
                onWagererRemoved = {},
                errorMessage = "Message",
                modifier = Modifier.padding(5.dp)
            )

            CreateButton(label = "Create goewg wlkejgw;ljg w;eag w;eagk", onClick = {})
        }


    }
}