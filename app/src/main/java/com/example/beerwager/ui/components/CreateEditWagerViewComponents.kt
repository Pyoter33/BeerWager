package com.example.beerwager.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SportsBar
import androidx.compose.material.icons.outlined.AddCircleOutline
import androidx.compose.material.icons.outlined.RemoveCircleOutline
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.example.beerwager.ui.state.BeersChangedEvent
import com.example.beerwager.ui.state.CreateWagersEvent

@Composable
fun BeersAtStakeView(
    beersAtStake: Int,
    onBeersChanged: (CreateWagersEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = {
            onBeersChanged(BeersChangedEvent(beersAtStake - 1))
        }) {
            Icon(Icons.Outlined.RemoveCircleOutline, "")
        }
        Text(
            text = beersAtStake.toString(),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Normal,
            color = Color.Black
        )
        Icon(Icons.Default.SportsBar, "")
        IconButton(onClick = {
            onBeersChanged(BeersChangedEvent(beersAtStake + 1))
        }) {
            Icon(Icons.Outlined.AddCircleOutline, "")
        }
    }

}

@Preview(showBackground = true)
@Composable
fun Test() {
    BeersAtStakeView(beersAtStake = 2, onBeersChanged = {})
}