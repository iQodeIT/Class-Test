package com.soulstice.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soulstice.app.data.local.dao.SoulsticeDao
import com.soulstice.app.data.local.entities.JournalEntry
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class JournalViewModel @Inject constructor(
    private val dao: SoulsticeDao
) : ViewModel() {

    val allEntries = dao.getAllJournalEntries()

    fun addEntry(content: String, isWin: Boolean = false, projectId: String? = null) {
        viewModelScope.launch {
            val entry = JournalEntry(
                id = UUID.randomUUID().toString(),
                content = content,
                date = System.currentTimeMillis(),
                projectId = projectId,
                isWin = isWin,
                createdAt = System.currentTimeMillis()
            )
            dao.insertJournalEntry(entry)
        }
    }
}
