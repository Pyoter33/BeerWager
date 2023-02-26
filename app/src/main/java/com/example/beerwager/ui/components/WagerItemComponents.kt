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
import com.example.beerwager.utils.ColorValues
import com.example.beerwager.utils.Dimen
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

val WAGER_BODY_HEIGHT= 90.dp
const val WAGER_BODY_WEIGHT= 0.60f
const val DATE_TIME_WEIGHT= 0.25f
const val AT_STAKE_WEIGHT= 0.15f
const val WAGER_COLUMN_WEIGHT= 0.90f
const val WAGER_ICON_WEIGHT = 0.10f
const val NUMBER_OF_WAGERERS_FOR_SINGLE_ICON = 2
const val NUMBER_OF_LINES = 2

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
                color = Wager.WAGER_COLORS[color].copy(ColorValues.ALPHA_HALF),
                RoundedCornerShape(corner = CornerSize(Dimen.CORNER_RADIUS_BIG))
            )
            .padding(Dimen.MARGIN_SMALL)
    ) {
        Row {
            Column(horizontalAlignment = Alignment.Start, modifier = Modifier
                .weight(WAGER_COLUMN_WEIGHT)
                .padding(bottom = Dimen.MARGIN_SMALL)
                .fillMaxWidth()
                .fillMaxHeight()) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = NUMBER_OF_LINES,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Normal,
                    maxLines = NUMBER_OF_LINES,
                    overflow = TextOverflow.Ellipsis,
                    color = Grey
                )

            }
            Icon(
                if (numberOfWagerers <= NUMBER_OF_WAGERERS_FOR_SINGLE_ICON) Icons.Filled.Person else Icons.Filled.Groups,
                contentDescription = stringResource(id = R.string.text_wagerers_indicator_icon),
                modifier = Modifier
                    .align(Alignment.Bottom)
                    .weight(WAGER_ICON_WEIGHT)
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
            modifier = Modifier.weight(DATE_TIME_WEIGHT)
        )
        MainWagerBody(
            title = wager.title,
            description = wager.description,
            color = wager.colour,
            numberOfWagerers = wager.wagerers.size,
            modifier = Modifier
                .weight(WAGER_BODY_WEIGHT)
                .height(WAGER_BODY_HEIGHT)
                .fillMaxWidth()
        )
        BeersAtStakeColumn(beersAtStake = wager.beersAtStake, modifier = Modifier.weight(AT_STAKE_WEIGHT))
    }
}