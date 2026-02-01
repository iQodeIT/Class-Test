package com.clearviewai.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clearviewai.app.data.local.entities.MediaItem
import com.clearviewai.app.data.repository.MediaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrashViewModel @Inject constructor(
    private val repository: MediaRepository
) : ViewModel() {

    val trashItems: StateFlow<List<MediaItem>> = repository.getTrashMedia()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun restore(item: MediaItem) {
        viewModelScope.launch {
            repository.updateStatus(item.id, "UNDECIDED")
        }
    }

    fun deleteItem(item: MediaItem) {
        viewModelScope.launch {
            repository.deletePermanently(item)
        }
    }

    fun emptyTrash() {
        viewModelScope.launch {
            trashItems.value.forEach {
                repository.deletePermanently(it)
            }
        }
    }
}
