package com.example.beerwager.ui.components.wagerlist

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.SportsBar
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.example.beerwager.R
import com.example.beerwager.data.data_source.Wager
import com.example.beerwager.ui.theme.Grey
import com.example.beerwager.utils.ColorValues
import com.example.beerwager.utils.Dimen
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

const val WAGER_BODY_WEIGHT= 0.80f
const val AT_STAKE_WEIGHT= 0.20f
const val NUMBER_OF_LINES = 2

@Composable
private fun MainWagerBody(
    wager: Wager,
    modifier: Modifier = Modifier
) {
    val formatStyle = FormatStyle.SHORT
    val dateFormatter = DateTimeFormatter.ofLocalizedDate(formatStyle)
    val timeFormatter = DateTimeFormatter.ofLocalizedTime(formatStyle)

    Box(
        modifier = modifier
            .background(
                color = Wager.WAGER_BACKGROUND_COLORS[wager.colour].copy(ColorValues.ALPHA_HALF),
                RoundedCornerShape(corner = CornerSize(Dimen.CORNER_RADIUS_BIG))
            )
            .padding(Dimen.SPACING_XS)
    ) {
            Column(horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = wager.title,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = NUMBER_OF_LINES,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.weight(1f)
                    )

                    if (wager.hasNotification) {
                        Icon(
                            Icons.Filled.Notifications,
                            contentDescription = stringResource(id = R.string.text_notification_icon)
                        )
                        Spacer(modifier = Modifier.size(Dimen.SPACING_XXS))
                    }
                }
                Text(
                    text = wager.description,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Normal,
                    maxLines = NUMBER_OF_LINES,
                    overflow = TextOverflow.Ellipsis,
                    color = Grey
                )
                Spacer(Modifier.size(Dimen.SPACING_XXS))
                Row {
                    Text(
                        text = wager.date.format(dateFormatter),
                        style = MaterialTheme.typography.labelMedium
                    )
                    Spacer(Modifier.size(Dimen.SPACING_XXS))
                    wager.time?.let {
                        Text(
                            text = it.format (timeFormatter)
                            ?: stringResource(id = R.string.text_all_day),
                        style = MaterialTheme.typography.labelMedium
                        )
                    }
                }
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
fun WagerItem(wager: Wager, onClick: (Long) -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick(wager.id) },
        verticalAlignment = Alignment.CenterVertically
    ) {
        MainWagerBody(
            wager = wager,
            modifier = Modifier
                .weight(WAGER_BODY_WEIGHT)
                .padding(start = Dimen.SPACING_M)
                .fillMaxWidth()
        )
        BeersAtStakeColumn(beersAtStake = wager.beersAtStake, modifier = Modifier.weight(
            AT_STAKE_WEIGHT
        ))
    }
}
