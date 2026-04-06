package com.example.beerwager.ui.components.createeditwager

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SportsBar
import androidx.compose.material.icons.outlined.AddCircleOutline
import androidx.compose.material.icons.outlined.RemoveCircleOutline
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.beerwager.ui.theme.Black
import com.example.beerwager.ui.theme.White
import com.example.beerwager.utils.ColorValues.ALPHA_SMALL
import com.example.beerwager.utils.Dimen

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
        Checkbox(
            checked = isChecked,
            enabled = !isBlocked,
            onCheckedChange = onCheckedChange,
            colors = CheckboxDefaults.colors(checkmarkColor = White)
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = Black,
            modifier = Modifier.padding(start = Dimen.SPACING_S)
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
                .padding(bottom = Dimen.SPACING_XS)
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
            modifier = Modifier.padding(Dimen.SPACING_XXS)
        )
    }
}
