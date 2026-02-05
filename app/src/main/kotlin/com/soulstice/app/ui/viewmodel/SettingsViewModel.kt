package com.soulstice.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soulstice.app.data.local.PreferenceManager
import android.util.Log
import com.soulstice.app.data.local.dao.SoulsticeDao
import com.soulstice.app.data.local.entities.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

@Serializable
data class ExportData(
    val tasks: List<Task>,
    val projects: List<Project>,
    val habits: List<Habit>,
    val resources: List<Resource>,
    val clients: List<Client>,
    val journalEntries: List<JournalEntry>,
    val inboxItems: List<InboxItem>
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val dao: SoulsticeDao,
    private val preferenceManager: PreferenceManager
) : ViewModel() {

    val pomodoroDuration = preferenceManager.pomodoroDuration
    val theme = preferenceManager.theme

    fun setPomodoroDuration(minutes: Int) {
        viewModelScope.launch {
            preferenceManager.setPomodoroDuration(minutes)
        }
    }

    fun setTheme(theme: String) {
        viewModelScope.launch {
            preferenceManager.setTheme(theme)
        }
    }

    fun resetDatabase() {
        viewModelScope.launch {
            dao.nukeDatabase()
        }
    }

    fun exportData() {
        viewModelScope.launch {
            val data = ExportData(
                tasks = dao.getAllTasks().first(),
                projects = dao.getAllProjects().first(),
                habits = dao.getAllHabits().first(),
                resources = dao.getAllResources().first(),
                clients = dao.getAllClients().first(),
                journalEntries = dao.getAllJournalEntries().first(),
                inboxItems = dao.getUnprocessedInboxItems().first()
            )
            val json = Json.encodeToString(data)
            Log.d("SoulsticeExport", json)
            // In a real app, we'd save to a file or share it
        }
    }

    fun importData(json: String) {
        viewModelScope.launch {
            try {
                val data = Json.decodeFromString<ExportData>(json)
                data.tasks.forEach { dao.insertTask(it) }
                data.projects.forEach { dao.insertProject(it) }
                data.habits.forEach { dao.insertHabit(it) }
                data.resources.forEach { dao.insertResource(it) }
                data.clients.forEach { dao.insertClient(it) }
                data.journalEntries.forEach { dao.insertJournalEntry(it) }
                data.inboxItems.forEach { dao.insertInboxItem(it) }
            } catch (e: Exception) {
                Log.e("SoulsticeImport", "Failed to import", e)
            }
        }
    }
}
