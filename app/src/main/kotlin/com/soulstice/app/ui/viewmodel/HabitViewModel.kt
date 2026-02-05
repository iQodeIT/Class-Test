package com.soulstice.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soulstice.app.data.local.dao.SoulsticeDao
import com.soulstice.app.data.local.entities.Habit
import com.soulstice.app.data.local.entities.HabitCompletion
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class HabitViewModel @Inject constructor(
    private val dao: SoulsticeDao
) : ViewModel() {

    data class HabitWithStatus(
        val habit: Habit,
        val isCompletedToday: Boolean,
        val streak: Int
    )

    val habitsWithStatus = combine(dao.getAllHabits(), dao.getAllTasks()) { habits, tasks ->
        // This is a simplification, ideally we use HabitCompletion table
        habits.map { habit ->
            // For now, use the streak field in Habit entity, but in real app we'd calculate from completions
            HabitWithStatus(habit, habit.lastCompleted != null && isToday(habit.lastCompleted), habit.streak)
        }
    }

    fun addHabit(title: String, category: String) {
        viewModelScope.launch {
            val habit = Habit(
                id = UUID.randomUUID().toString(),
                title = title,
                frequency = "daily",
                streak = 0,
                lastCompleted = null,
                createdAt = System.currentTimeMillis()
            )
            dao.insertHabit(habit)
        }
    }

    fun completeHabit(habit: Habit) {
        viewModelScope.launch {
            val now = System.currentTimeMillis()
            if (habit.lastCompleted != null && isToday(habit.lastCompleted)) return@launch

            val newStreak = if (habit.lastCompleted != null && isYesterday(habit.lastCompleted)) habit.streak + 1 else 1
            dao.insertHabit(habit.copy(lastCompleted = now, streak = newStreak))

            dao.insertHabitCompletion(HabitCompletion(
                id = UUID.randomUUID().toString(),
                habitId = habit.id,
                date = now,
                createdAt = now
            ))
        }
    }

    private fun isToday(timestamp: Long): Boolean {
        // Simple day check
        return (System.currentTimeMillis() / (24 * 60 * 60 * 1000)) == (timestamp / (24 * 60 * 60 * 1000))
    }

    private fun isYesterday(timestamp: Long): Boolean {
        return (System.currentTimeMillis() / (24 * 60 * 60 * 1000)) - 1 == (timestamp / (24 * 60 * 60 * 1000))
    }
}
