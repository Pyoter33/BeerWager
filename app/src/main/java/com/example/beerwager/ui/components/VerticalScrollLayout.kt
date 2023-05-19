package com.example.beerwager.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class ChildLayout(
    val items: List<Any>? = null,
    val content: @Composable (index: Int?, item: Any?) -> Unit
)

@Composable
fun VerticalScrollLayout(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    vararg childLayouts: ChildLayout
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = contentPadding,
        verticalArrangement = verticalArrangement
    ) {
        childLayouts.forEach { child ->
            child.items?.let { loadItems(child) } ?: loadItem(child)
        }
    }
}

/**
 * Use single item compose if no scroll or only horizontal scroll needed
 */
private fun LazyListScope.loadItem(childLayout: ChildLayout) {
    item {
        childLayout.content(null, null)
    }
}

/**
 * Use load multiple items to the lazy column when nested vertical scroll is needed
 */
private fun LazyListScope.loadItems(childLayout: ChildLayout) {
    itemsIndexed(items = childLayout.items!!) { index, item ->
        childLayout.content(index, item)
    }
}