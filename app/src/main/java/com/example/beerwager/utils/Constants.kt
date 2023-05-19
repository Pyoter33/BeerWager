package com.example.beerwager.utils

import androidx.compose.ui.unit.dp

object Dimen {
    val BORDER_UNFOCUSED = 1.dp
    val BORDER_FOCUSED = 2.dp
    val MARGIN_SMALL = 4.dp
    val MARGIN_MEDIUM = 8.dp
    val MARGIN_BIG = 12.dp
    val MARGIN_LARGE = 16.dp
    val MARGIN_XLARGE = 20.dp
    val MARGIN_XXLARGE = 32.dp
    val SWITCH_MARGIN = 30.dp
    val CORNER_RADIUS_XSMALL = 2.dp
    val CORNER_RADIUS_SMALL = 5.dp
    val CORNER_RADIUS_BIG = 10.dp
    val ICON_SIZE_BIG = 32.dp
    val ICON_SIZE_XXLARGE = 72.dp
    val SMALL_TEXT_FIELD_WIDTH = 180.dp
    val LONG_TEXT_FIELD_HEIGHT = 100.dp
    val COLOUR_BOX_SIZE = 36.dp
}

object ColorValues {
    const val ALPHA_HALF = 0.5f
    const val ALPHA_DISABLED = 0.38f
    const val ALPHA_SMALL = 0.1f
}

object NavigationArgs {
    const val ARG_SEARCH_QUERY = "searchQuery"
    const val ARG_WAGER_ID = "wagerId"
    const val ARG_CATEGORY = "category"
    const val ARG_EMPTY_STRING = ""
}