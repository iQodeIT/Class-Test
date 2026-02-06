package com.birthday.planner.data.repository

import com.birthday.planner.data.dao.*
import com.birthday.planner.data.model.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PartyRepository @Inject constructor(
    private val partyDao: PartyDao,
    private val boardDao: BoardDao,
    private val pinDao: PinDao,
    private val checklistDao: ChecklistDao,
    private val budgetDao: BudgetDao
) {
    // Parties
    val allParties: Flow<List<Party>> = partyDao.getAllParties()
    suspend fun getPartyById(id: Long) = partyDao.getPartyById(id)
    suspend fun insertParty(party: Party) = partyDao.insertParty(party)
    suspend fun updateParty(party: Party) = partyDao.updateParty(party)
    suspend fun deleteParty(party: Party) = partyDao.deleteParty(party)

    // Boards
    fun getBoardsForParty(partyId: Long) = boardDao.getBoardsForParty(partyId)
    suspend fun insertBoard(board: Board) = boardDao.insertBoard(board)
    suspend fun deleteBoard(board: Board) = boardDao.deleteBoard(board)

    // Pins
    fun getPinsForBoard(boardId: Long) = pinDao.getPinsForBoard(boardId)
    suspend fun insertPin(pin: Pin) = pinDao.insertPin(pin)
    suspend fun updatePin(pin: Pin) = pinDao.updatePin(pin)
    suspend fun deletePin(pin: Pin) = pinDao.deletePin(pin)

    // Checklist
    fun getChecklistForParty(partyId: Long) = checklistDao.getChecklistForParty(partyId)
    suspend fun insertChecklistItem(item: ChecklistItem) = checklistDao.insertChecklistItem(item)
    suspend fun updateChecklistItem(item: ChecklistItem) = checklistDao.updateChecklistItem(item)
    suspend fun deleteChecklistItem(item: ChecklistItem) = checklistDao.deleteChecklistItem(item)

    // Budget
    fun getBudgetForParty(partyId: Long) = budgetDao.getBudgetForParty(partyId)
    suspend fun insertBudgetItem(item: BudgetItem) = budgetDao.insertBudgetItem(item)
    suspend fun updateBudgetItem(item: BudgetItem) = budgetDao.updateBudgetItem(item)
    suspend fun deleteBudgetItem(item: BudgetItem) = budgetDao.deleteBudgetItem(item)
}
