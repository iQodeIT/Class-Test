package com.soulstice.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soulstice.app.data.local.dao.SoulsticeDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val dao: SoulsticeDao
) : ViewModel() {

    private val _pomodoroDuration = MutableStateFlow(25)
    val pomodoroDuration: StateFlow<Int> = _pomodoroDuration

    private val _theme = MutableStateFlow("Light")
    val theme: StateFlow<String> = _theme

    fun setPomodoroDuration(minutes: Int) {
        _pomodoroDuration.value = minutes
    }

    fun setTheme(theme: String) {
        _theme.value = theme
    }

    fun resetDatabase() {
        viewModelScope.launch {
            // This is a bit tricky with Room in a ViewModel without the DB instance
            // but for this MVP we can just delete everything via DAO if we have methods
            // or just leave as a placeholder that shows a Toast in a real app.
        }
    }
}
