package com.example.todoappcompose.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color



@Immutable
data class ExtendedColors(
    val chipBackground : Color,
    val chipSelected : Color,
    val priorityHigh: Color,
    val priorityMedium: Color,
    val priorityLow: Color,
    val priorityNone: Color,
)

val LocalExtendedColors = staticCompositionLocalOf {
    ExtendedColors(
        chipBackground = Color.Unspecified,
        chipSelected = Color.Unspecified,
        priorityHigh = Color.Unspecified,
        priorityMedium = Color.Unspecified,
        priorityLow = Color.Unspecified,
        priorityNone = Color.Unspecified,
    )
}


object ExtendedTheme {
    val colors: ExtendedColors
        @Composable
        get() = LocalExtendedColors.current
}