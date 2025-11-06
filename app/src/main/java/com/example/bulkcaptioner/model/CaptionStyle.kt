package com.example.bulkcaptioner.model

import androidx.compose.ui.graphics.Color

enum class AnimationType {
    NONE, FADE_IN, SLIDE_UP, WORD_BY_WORD, POP
}

data class CaptionStyle(
    val fontFamily: String = "sans-serif",
    val fontSize: Int = 48, // sp
    val isBold: Boolean = true,
    val isItalic: Boolean = false,
    val textColor: Color = Color.White,
    val strokeColor: Color = Color.Black,
    val strokeWidth: Float = 4f,
    val hasBackground: Boolean = true,
    val backgroundColor: Color = Color.Black.copy(alpha = 0.6f),
    val shadowEnabled: Boolean = true,
    val shadowColor: Color = Color.Black,
    val animation: AnimationType = AnimationType.FADE_IN
)
