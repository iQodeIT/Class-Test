package com.wedding.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wedding.app.data.local.entity.BoardEntity
import com.wedding.app.data.local.entity.BudgetItemEntity
import com.wedding.app.data.local.entity.ChecklistItemEntity
import com.wedding.app.data.local.entity.PinEntity
import com.wedding.app.data.local.entity.UserEntity
import com.wedding.app.data.local.entity.WeddingEntity
import com.wedding.app.data.repository.WeddingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class WeddingViewModel(private val repository: WeddingRepository) : ViewModel() {

    val user: StateFlow<UserEntity?> = repository.user.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), null
    )

    val weddings: StateFlow<List<WeddingEntity>> = repository.weddings.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
    )

    private val _currentWedding = MutableStateFlow<WeddingEntity?>(null)
    val currentWedding = _currentWedding.asStateFlow()

    fun selectWedding(wedding: WeddingEntity) {
        _currentWedding.value = wedding
    }

    fun getBoards(weddingId: Long) = repository.getBoards(weddingId)
    fun getPins(boardId: Long) = repository.getPins(boardId)
    fun getPin(pinId: Long) = repository.getPin(pinId)
    fun getChecklist(weddingId: Long) = repository.getChecklist(weddingId)
    fun getBudgetItems(weddingId: Long) = repository.getBudgetItems(weddingId)

    fun addPin(boardId: Long, imageUrl: String, notes: String? = null, price: Double? = null) {
        viewModelScope.launch {
            repository.insertPin(PinEntity(boardId = boardId, imageUrl = imageUrl, notes = notes, price = price))
        }
    }

    fun toggleChecklistItem(item: ChecklistItemEntity) {
        viewModelScope.launch {
            repository.updateChecklistItem(item.copy(isCompleted = !item.isCompleted))
        }
    }

    fun addChecklistItem(weddingId: Long, title: String, phase: String) {
        viewModelScope.launch {
            repository.insertChecklistItem(ChecklistItemEntity(weddingId = weddingId, title = title, phase = phase, isCompleted = false, dueDate = null))
        }
    }

    fun addBudgetItem(weddingId: Long, category: String, amount: Double) {
        viewModelScope.launch {
            repository.insertBudgetItem(BudgetItemEntity(weddingId = weddingId, category = category, allocatedAmount = amount))
        }
    }

    fun markPinAsFinal(pinId: Long, boardId: Long) {
        viewModelScope.launch {
            repository.selectFinalPin(pinId, boardId)
        }
    }

    fun createUser(name: String, partnerName: String?) {
        viewModelScope.launch {
            repository.insertUser(UserEntity(name = name, partnerName = partnerName))
        }
    }

    fun addWedding(name: String, date: Long, location: String?, minBudget: Double, maxBudget: Double) {
        viewModelScope.launch {
            val id = repository.insertWedding(
                WeddingEntity(
                    name = name,
                    date = date,
                    location = location,
                    budgetRangeMin = minBudget,
                    budgetRangeMax = maxBudget
                )
            )
            // Optionally select it or initialize default boards
            initializeDefaultBoards(id)
        }
    }

    private suspend fun initializeDefaultBoards(weddingId: Long) {
        val defaultBoards = listOf(
            "Venue & Decor", "Dresses & Attire", "Color Palette", "Flowers & Bouquets",
            "Cake & Catering", "Photography Style", "Invitations & Stationery", "Hair & Makeup"
        )
        defaultBoards.forEachIndexed { index, name ->
            repository.insertBoard(BoardEntity(weddingId = weddingId, name = name, order = index))
        }

        val defaultChecklist = listOf(
            "Book Venue" to "12 Months Out",
            "Select Photographer" to "12 Months Out",
            "Buy Wedding Dress" to "9 Months Out",
            "Order Cake" to "6 Months Out",
            "Send Invitations" to "3 Months Out",
            "Final Fitting" to "1 Month Out"
        )
        defaultChecklist.forEach { (title, phase) ->
            repository.insertChecklistItem(ChecklistItemEntity(weddingId = weddingId, title = title, phase = phase, isCompleted = false, dueDate = null))
        }

        val defaultBudget = listOf(
            "Venue", "Catering", "Decor", "Attire", "Photography", "Music", "Stationery"
        )
        defaultBudget.forEach { category ->
            repository.insertBudgetItem(BudgetItemEntity(weddingId = weddingId, category = category, allocatedAmount = 1000.0))
        }
    }
}
