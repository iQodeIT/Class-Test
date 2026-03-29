package com.soulstice.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.soulstice.app.data.local.dao.SoulsticeDao
import com.soulstice.app.data.local.entities.InboxItem
import com.soulstice.app.data.local.entities.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class InboxViewModel @Inject constructor(
    private val dao: SoulsticeDao
) : ViewModel() {

    val inboxItems: Flow<List<InboxItem>> = dao.getUnprocessedInboxItems()

    fun addItem(content: String) {
        viewModelScope.launch {
            val item = InboxItem(
                id = UUID.randomUUID().toString(),
                content = content,
                status = "unprocessed",
                createdAt = System.currentTimeMillis()
            )
            dao.insertInboxItem(item)
        }
    }

    fun processToTask(item: InboxItem, type: String = "task") {
        viewModelScope.launch {
            val task = Task(
                id = UUID.randomUUID().toString(),
                title = item.content,
                description = null,
                status = "todo",
                type = type,
                priority = "medium",
                energy = "high",
                projectId = null,
                dueDate = null,
                createdAt = System.currentTimeMillis()
            )
            dao.insertTask(task)
            dao.updateInboxItem(item.copy(status = "processed"))
        }
    }

    fun archiveItem(item: InboxItem) {
        viewModelScope.launch {
            dao.updateInboxItem(item.copy(status = "processed"))
        }
    }

    fun deleteItem(item: InboxItem) {
        viewModelScope.launch {
            dao.deleteInboxItem(item)
        }
    }
}
