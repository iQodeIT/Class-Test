package com.clearviewai.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.clearviewai.app.data.local.entities.MediaItem
import com.clearviewai.app.data.repository.MediaRepository
import com.clearviewai.app.domain.CullingEngine
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SwipeViewModel @Inject constructor(
    private val repository: MediaRepository,
    private val cullingEngine: CullingEngine
) : ViewModel() {

    private val _items = MutableStateFlow<List<MediaItem>>(emptyList())
    val items: StateFlow<List<MediaItem>> = _items.asStateFlow()

    private val _currentIndex = MutableStateFlow(0)
    val currentIndex: StateFlow<Int> = _currentIndex.asStateFlow()

    init {
        viewModelScope.launch {
            repository.syncWithMediaStore()
            repository.getUndecidedMedia().collect { list ->
                _items.value = list
                // Optionally analyze items in background
                analyzeCurrentItems(list)
            }
        }
    }

    private fun analyzeCurrentItems(list: List<MediaItem>) {
        viewModelScope.launch {
            list.take(10).forEach { item ->
                if (item.score == 0f) {
                    val analyzed = cullingEngine.analyze(item, list)
                    // Update in DB (which will trigger collection)
                    // But to avoid infinite loop of sync, we should be careful.
                    // Here we just update the specific item.
                }
            }
        }
    }

    fun swipeLeft() { // Delete
        val currentItem = currentItem() ?: return
        viewModelScope.launch {
            repository.updateStatus(currentItem.id, "TRASH")
            next()
        }
    }

    fun markAllClutterAsTrash() {
        viewModelScope.launch {
            _items.value.forEach { item ->
                if (item.clutterType != null || item.isBlurry) {
                    repository.updateStatus(item.id, "TRASH")
                }
            }
        }
    }

    fun swipeRight() { // Keep
        val currentItem = currentItem() ?: return
        viewModelScope.launch {
            repository.updateStatus(currentItem.id, "KEEP")
            next()
        }
    }

    fun swipeUp() { // Organize
        val currentItem = currentItem() ?: return
        viewModelScope.launch {
            // For now, just mark as KEEP or a new status ORGANIZE
            repository.updateStatus(currentItem.id, "KEEP")
            next()
        }
    }

    fun next() {
        if (_currentIndex.value < _items.value.size - 1) {
            _currentIndex.value++
        }
    }

    fun previous() {
        if (_currentIndex.value > 0) {
            _currentIndex.value--
        }
    }

    private fun currentItem(): MediaItem? {
        val list = _items.value
        val index = _currentIndex.value
        return if (index in list.indices) list[index] else null
    }
}
