package com.soulstice.app.data.local

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(name = "settings")

@Singleton
class PreferenceManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val ENERGY_LEVEL = stringPreferencesKey("energy_level")
    private val THEME = stringPreferencesKey("theme")
    private val POMODORO_DURATION = intPreferencesKey("pomodoro_duration")

    val energyLevel: Flow<String> = context.dataStore.data.map { it[ENERGY_LEVEL] ?: "high" }
    val theme: Flow<String> = context.dataStore.data.map { it[THEME] ?: "Light" }
    val pomodoroDuration: Flow<Int> = context.dataStore.data.map { it[POMODORO_DURATION] ?: 25 }

    suspend fun setEnergyLevel(level: String) {
        context.dataStore.edit { it[ENERGY_LEVEL] = level }
    }

    suspend fun toggleEnergy() {
        context.dataStore.edit {
            val current = it[ENERGY_LEVEL] ?: "high"
            it[ENERGY_LEVEL] = if (current == "high") "low" else "high"
        }
    }

    suspend fun setTheme(theme: String) {
        context.dataStore.edit { it[THEME] = theme }
    }

    suspend fun setPomodoroDuration(minutes: Int) {
        context.dataStore.edit { it[POMODORO_DURATION] = minutes }
    }
}
