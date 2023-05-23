package com.example.beerwager.ui.components.createeditwager

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.AddCircleOutline
import androidx.compose.material.icons.outlined.RemoveCircleOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.example.beerwager.R
import com.example.beerwager.domain.models.WagerFilter
import com.example.beerwager.ui.theme.*
import com.example.beerwager.utils.ColorValues.ALPHA_SMALL
import com.example.beerwager.utils.Dimen
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
