package com.birthday.planner.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.birthday.planner.data.model.PartyType
import com.birthday.planner.data.model.ThemeSuggestion
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ThemeGeneratorViewModel @Inject constructor() : ViewModel() {
    private val _step = MutableStateFlow(1)
    val step = _step.asStateFlow()

    private val _age = MutableStateFlow(10)
    val age = _age.asStateFlow()

    private val _partyType = MutableStateFlow(PartyType.KIDS)
    val partyType = _partyType.asStateFlow()

    private val _suggestions = MutableStateFlow<List<ThemeSuggestion>>(emptyList())
    val suggestions = _suggestions.asStateFlow()

    fun nextStep() {
        if (_step.value < 3) {
            _step.value += 1
        } else {
            generateThemes()
            _step.value = 4 // Results step
        }
    }

    fun setAge(age: Int) {
        _age.value = age
        _partyType.value = when {
            age < 13 -> PartyType.KIDS
            age < 18 -> PartyType.TEENS
            else -> PartyType.ADULT
        }
    }

    private fun generateThemes() {
        val type = _partyType.value
        val age = _age.value

        _suggestions.value = when (type) {
            PartyType.KIDS -> listOf(
                ThemeSuggestion("Superhero Academy", listOf("FF0000", "0000FF", "FFFF00"), listOf("Decor", "Games", "Cake"), "🦸"),
                ThemeSuggestion("Unicorn Dreams", listOf("FFC0CB", "E6E6FA", "FFFFE0"), listOf("Rainbow Decor", "Sparkle Snacks"), "🦄"),
                ThemeSuggestion("Space Explorer", listOf("000080", "C0C0C0", "FFA500"), listOf("Galaxy Cake", "Moon Landing Games"), "🚀")
            )
            PartyType.TEENS -> listOf(
                ThemeSuggestion("Neon Glow Party", listOf("39FF14", "FF00FF", "00FFFF"), listOf("Neon Outfits", "Music Mix"), "🕺"),
                ThemeSuggestion("Gaming Marathon", listOf("4B0082", "00FF00", "000000"), listOf("Tournament Setup", "Pixel Snacks"), "🎮"),
                ThemeSuggestion("TikTok Challenge", listOf("000000", "EE1D52", "69C9D0"), listOf("Photo Booth", "Trendy Decor"), "📱")
            )
            PartyType.ADULT -> listOf(
                ThemeSuggestion("Elegant Garden", listOf("556B2F", "FFFDD0", "8B4513"), listOf("Floral Setup", "Fine Dining"), "🌸"),
                ThemeSuggestion("Casino Royale", listOf("000000", "FFD700", "FF0000"), listOf("Poker Table", "Cocktails"), "🎲"),
                ThemeSuggestion("Retro 80s", listOf("FF1493", "00CED1", "FFD700"), listOf("Vintage Decor", "Arcade Games"), "📻")
            )
        }
    }
}
