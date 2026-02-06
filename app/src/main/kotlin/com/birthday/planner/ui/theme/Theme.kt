package com.birthday.planner.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

import com.birthday.planner.data.model.PartyType

private val KidsColorScheme = lightColorScheme(
    primary = KidsPrimary,
    secondary = KidsSecondary,
    tertiary = SunsetOrange,
    background = Cream,
    surface = Color.White,
    onPrimary = DeepCharcoal,
    onSecondary = Color.White,
    onBackground = DeepCharcoal,
    onSurface = DeepCharcoal,
)

private val TeensColorScheme = lightColorScheme(
    primary = TeensPrimary,
    secondary = TeensSecondary,
    tertiary = ElectricPurple,
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color.White,
    onSurface = Color.White,
)

private val AdultsColorScheme = lightColorScheme(
    primary = AdultsPrimary,
    secondary = AdultsSecondary,
    tertiary = BrightTeal,
    background = Color(0xFFFAF7F5),
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = DeepCharcoal,
    onBackground = DeepCharcoal,
    onSurface = DeepCharcoal,
)

private val DefaultColorScheme = lightColorScheme(
    primary = Coral,
    secondary = ElectricPurple,
    tertiary = BrightTeal,
    background = Cream,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = DeepCharcoal,
    onSurface = DeepCharcoal,
)

@Composable
fun BirthdayPlannerTheme(
    partyType: PartyType? = null,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when (partyType) {
        PartyType.KIDS -> KidsColorScheme
        PartyType.TEENS -> TeensColorScheme
        PartyType.ADULT -> AdultsColorScheme
        else -> if (darkTheme) {
            darkColorScheme(
                primary = Coral,
                background = DeepCharcoal,
                surface = DeepCharcoal,
                onPrimary = Cream,
                onBackground = Cream,
                onSurface = Cream
            )
        } else DefaultColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
