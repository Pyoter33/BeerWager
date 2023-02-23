package com.example.beerwager.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import com.example.beerwager.data.data_source.Wager
import com.example.beerwager.domain.models.WagerFilter

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WagerList(wagers: Map<String, List<Wager>>, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier) {
        wagers.forEach { (category, wagers) ->
            item(key = category) {
                WagerListHeader(
                    headerTitle = category,
                    Modifier.padding(8.dp).animateItemPlacement(tween())
                )
            }
            items(wagers, key = { it.id!! }) {
                if (category == WagerFilter.CLOSED.toString()) {
                    WagerItem(wager = it, Modifier.alpha(0.5f).animateItemPlacement(tween()))
                } else {
                    WagerItem(wager = it, Modifier.animateItemPlacement(tween()))
                }
                Spacer(modifier = Modifier.padding(8.dp))
            }
        }
    }
}

@Composable
private fun WagerListHeader(headerTitle: String, modifier: Modifier = Modifier) {
    Text(text = headerTitle, style = MaterialTheme.typography.titleMedium, modifier = modifier)
}