package com.wedding.app.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = BlushPink,
    secondary = ChampagneGold,
    tertiary = SageGreen,
    background = DarkCharcoal,
    surface = DarkCharcoal,
    onPrimary = Charcoal,
    onSecondary = Charcoal,
    onTertiary = Charcoal,
    onBackground = WarmWhite,
    onSurface = WarmWhite,
)

private val LightColorScheme = lightColorScheme(
    primary = BlushPink,
    secondary = ChampagneGold,
    tertiary = SageGreen,
    background = WarmWhite,
    surface = WarmWhite,
    onPrimary = Charcoal,
    onSecondary = Charcoal,
    onTertiary = Charcoal,
    onBackground = Charcoal,
    onSurface = Charcoal,
)

@Composable
fun WeddingAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
