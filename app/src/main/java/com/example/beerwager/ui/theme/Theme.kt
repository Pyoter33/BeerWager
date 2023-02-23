package com.example.beerwager.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightThemeColors = lightColorScheme(
    primary = Green,
    onPrimary = Black,
    background = White,
    onBackground = Black
)

@Composable
fun AppTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(colorScheme = LightThemeColors, typography = Typography) {
        content()
    }
}