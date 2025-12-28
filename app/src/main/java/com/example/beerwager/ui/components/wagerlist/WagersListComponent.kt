package com.example.beerwager.ui.components.wagerlist

import androidx.compose.animation.core.tween
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
import com.example.beerwager.data.data_source.Wager
import com.example.beerwager.domain.models.WagerCategory
import com.example.beerwager.utils.ColorValues
import com.example.beerwager.utils.Dimen

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun WagerList(wagers: Map<WagerCategory, List<Wager>>, onWagerClick: (Long) -> Unit, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier) {
        wagers.forEach { (category, wagers) ->
            item(key = category) {
                WagerListHeader(
                    category = category,
                    Modifier
                        .padding(Dimen.SPACING_XS)
                        .animateItem(placementSpec = tween())
                )
            }
            items(wagers, key = { it.id }) {
                if (category == WagerCategory.CLOSED) {
                    WagerItem(wager = it, onWagerClick,
                        Modifier
                            .alpha(ColorValues.ALPHA_HALF)
                            .animateItem(placementSpec = tween()))
                } else {
                    WagerItem(wager = it, onWagerClick, Modifier.animateItem(placementSpec = tween()))
                }
                Spacer(modifier = Modifier.padding(Dimen.SPACING_XS))
            }
        }
        item {
            Spacer(modifier = Modifier.padding(Dimen.SPACING_XXL))
        }
    }
}

@Composable
private fun WagerListHeader(category: WagerCategory, modifier: Modifier = Modifier) {
    Text(text = category.toString(), style = MaterialTheme.typography.titleMedium, modifier = modifier)
}