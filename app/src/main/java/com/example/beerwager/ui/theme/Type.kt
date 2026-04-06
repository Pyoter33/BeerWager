package com.example.beerwager.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.example.beerwager.R


private val AppFontFamily = FontFamily(
    fonts = listOf(
        Font(
            resId = R.font.hahmlet_regular,
            weight = FontWeight.Normal
        ),
        Font(
            resId = R.font.hahmlet_medium,
            weight = FontWeight.Medium
        ),
        Font(
            resId = R.font.hahmlet_bold,
            weight = FontWeight.Bold
        )
    )
)

val Typography = Typography().copy(
    titleLarge = Typography().titleLarge.copy(fontFamily = AppFontFamily),
    titleMedium = Typography().titleMedium.copy(fontFamily = AppFontFamily),
    headlineLarge = Typography().headlineLarge.copy(fontFamily = AppFontFamily),
    headlineMedium = Typography().headlineMedium.copy(fontFamily = AppFontFamily),
    headlineSmall = Typography().headlineSmall.copy(fontFamily = AppFontFamily),
    bodyLarge = Typography().bodyLarge.copy(fontFamily = AppFontFamily),
    bodyMedium = Typography().bodyMedium.copy(fontFamily = AppFontFamily),
    bodySmall = Typography().bodySmall.copy(fontFamily = AppFontFamily),
    labelLarge = Typography().labelLarge.copy(fontFamily = AppFontFamily),
    labelMedium = Typography().labelMedium.copy(fontFamily = AppFontFamily),
    labelSmall = Typography().labelSmall.copy(fontFamily = AppFontFamily),
)