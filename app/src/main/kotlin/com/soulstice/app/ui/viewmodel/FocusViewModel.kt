package com.soulstice.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soulstice.app.data.local.dao.SoulsticeDao
import com.soulstice.app.data.local.entities.FocusSession
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class FocusViewModel @Inject constructor(
    private val dao: SoulsticeDao
) : ViewModel() {

    private val _timeLeft = MutableStateFlow(25 * 60 * 1000L)
    val timeLeft: StateFlow<Long> = _timeLeft

    private val _isRunning = MutableStateFlow(false)
    val isRunning: StateFlow<Boolean> = _isRunning

    val sessionsToday = dao.getFocusSessionCountForDay(getStartOfToday())

    private var timerJob: Job? = null

    fun toggleTimer() {
        if (_isRunning.value) {
            stopTimer()
        } else {
            startTimer()
        }
    }

    private fun startTimer() {
        _isRunning.value = true
        timerJob = viewModelScope.launch {
            while (_timeLeft.value > 0) {
                delay(1000L)
                _timeLeft.value -= 1000L
            }
            onTimerFinished()
        }
    }

    private fun stopTimer() {
        _isRunning.value = false
        timerJob?.cancel()
    }

    fun resetTimer() {
        stopTimer()
        _timeLeft.value = 25 * 60 * 1000L
    }

    private fun onTimerFinished() {
        stopTimer()
        viewModelScope.launch {
            val session = FocusSession(
                id = UUID.randomUUID().toString(),
                duration = 25 * 60 * 1000L,
                mode = "work",
                date = System.currentTimeMillis(),
                createdAt = System.currentTimeMillis()
            )
            dao.insertFocusSession(session)
        }
        resetTimer()
    }

    private fun getStartOfToday(): Long {
        return (System.currentTimeMillis() / (24 * 60 * 60 * 1000)) * (24 * 60 * 60 * 1000)
    }
}
