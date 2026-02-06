package com.apartment.planner.ui.theme

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Spacing(
    val default: Dp = 0.dp,
    val tight: Dp = 4.dp,
    val compact: Dp = 8.dp,
    val standard: Dp = 16.dp,
    val comfortable: Dp = 24.dp,
    val spacious: Dp = 32.dp,
    val extraSpacious: Dp = 48.dp
)

val LocalSpacing = compositionLocalOf { Spacing() }
