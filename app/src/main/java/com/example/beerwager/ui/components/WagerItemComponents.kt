package com.example.beerwager.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SportsBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.beerwager.R
import com.example.beerwager.data.data_source.Wager
import com.example.beerwager.ui.theme.Grey
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
private fun DateTimeColumn(
    date: LocalDate,
    time: LocalTime?,
    hasNotification: Boolean,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        val formatStyle = FormatStyle.SHORT
        val dateFormatter = DateTimeFormatter.ofLocalizedDate(formatStyle)
        val timeFormatter = DateTimeFormatter.ofLocalizedTime(formatStyle)
        Text(text = date.format(dateFormatter), style = MaterialTheme.typography.bodySmall)
        Text(
            text = time?.format(timeFormatter) ?: stringResource(id = R.string.text_all_day),
            style = MaterialTheme.typography.bodySmall
        )
        if (hasNotification) {
            Icon(
                Icons.Filled.Notifications,
                contentDescription = stringResource(id = R.string.text_notification_icon)
            )
        }
    }
}

@Composable
private fun MainWagerBody(
    title: String,
    description: String,
    color: Int,
    numberOfWagerers: Int,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(
                color = Wager.WAGER_COLORS[color].copy(0.5f),
                RoundedCornerShape(corner = CornerSize(10.dp))
            )
            .padding(5.dp)
    ) {
        Row {
            Column(horizontalAlignment = Alignment.Start, modifier = Modifier
                .weight(0.9f)
                .padding(bottom = 5.dp)
                .fillMaxWidth()
                .fillMaxHeight()) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Normal,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    color = Grey
                )

            }
            Icon(
                if (numberOfWagerers < 2) Icons.Filled.Person else Icons.Filled.Groups,
                contentDescription = stringResource(id = R.string.text_wagerers_indicator_icon),
                modifier = Modifier
                    .align(Alignment.Bottom)
                    .weight(0.1f)
            )
        }
    }
}

@Composable
private fun BeersAtStakeColumn(
    beersAtStake: Int,
    modifier: Modifier = Modifier
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = beersAtStake.toString(),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )
            Icon(
                Icons.Filled.SportsBar,
                contentDescription = stringResource(id = R.string.text_beer_icon)
            )
        }
        Text(
            text = stringResource(id = R.string.text_at_stake),
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun WagerItem(wager: Wager, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
        verticalAlignment = Alignment.CenterVertically
    ) {
        DateTimeColumn(
            date = wager.date,
            time = wager.time,
            hasNotification = wager.hasNotification,
            modifier = Modifier.weight(0.25f)
        )
        MainWagerBody(
            title = wager.title,
            description = wager.description,
            color = wager.colour,
            numberOfWagerers = wager.wagerers.size,
            modifier = Modifier
                .weight(0.60f)
                .height(90.dp)
                .fillMaxWidth()
        )
        BeersAtStakeColumn(beersAtStake = wager.beersAtStake, modifier = Modifier.weight(0.15f))
    }
}