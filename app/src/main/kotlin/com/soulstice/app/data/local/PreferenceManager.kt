package com.soulstice.app.data.local

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferenceManager @Inject constructor() {
    private val _energyLevel = MutableStateFlow("high")
    val energyLevel: StateFlow<String> = _energyLevel

    fun setEnergyLevel(level: String) {
        _energyLevel.value = level
    }

    fun toggleEnergy() {
        _energyLevel.value = if (_energyLevel.value == "high") "low" else "high"
    }
}
