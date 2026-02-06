package com.birthday.planner.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.birthday.planner.data.model.*
import com.birthday.planner.data.repository.PartyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class PartyViewModel @Inject constructor(
    private val repository: PartyRepository
) : ViewModel() {

    val allParties = repository.allParties.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList()
    )

    private val _selectedParty = MutableStateFlow<Party?>(null)
    val selectedParty = _selectedParty.asStateFlow()

    fun selectParty(partyId: Long) {
        viewModelScope.launch {
            _selectedParty.value = repository.getPartyById(partyId)
        }
    }

    fun createParty(party: Party) {
        viewModelScope.launch {
            repository.insertParty(party)
        }
    }

    // Boards for selected party
    val boards = selectedParty.flatMapLatest { party ->
        if (party != null) {
            repository.getBoardsForParty(party.id).flatMapLatest { boardList ->
                val boardWithPinsFlows = boardList.map { board ->
                    repository.getPinsForBoard(board.id).map { pins -> board to pins.size }
                }
                if (boardWithPinsFlows.isEmpty()) flowOf(emptyList())
                else combine(boardWithPinsFlows) { it.toList() }
            }
        } else flowOf(emptyList())
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addBoard(name: String, emoji: String, gradient: String) {
        val partyId = _selectedParty.value?.id ?: return
        viewModelScope.launch {
            repository.insertBoard(Board(partyId = partyId, boardName = name, emoji = emoji, gradient = gradient))
        }
    }

    // Checklist for selected party
    val checklist = selectedParty.flatMapLatest { party ->
        if (party != null) repository.getChecklistForParty(party.id)
        else flowOf(emptyList())
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun toggleChecklistItem(item: ChecklistItem) {
        viewModelScope.launch {
            repository.updateChecklistItem(item.copy(isCompleted = !item.isCompleted))
        }
    }

    // Budget for selected party
    val budgetItems = selectedParty.flatMapLatest { party ->
        if (party != null) repository.getBudgetForParty(party.id)
        else flowOf(emptyList())
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
}
