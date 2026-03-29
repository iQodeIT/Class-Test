package com.soulstice.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = Sage600,
    secondary = Clay500,
    tertiary = Taupe500,
    background = Sage50,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Taupe900,
    onSurface = Taupe900,
)

private val DarkColorScheme = darkColorScheme(
    primary = Sage500,
    secondary = Clay500,
    tertiary = Taupe500,
    background = Color(0xFF1A1C1A),
    surface = Color(0xFF121412),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Sage50,
    onSurface = Sage50,
)

@Composable
fun SoulsticeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
