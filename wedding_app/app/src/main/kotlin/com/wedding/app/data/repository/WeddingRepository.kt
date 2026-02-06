package com.wedding.app.data.repository

import com.wedding.app.data.local.dao.WeddingDao
import com.wedding.app.data.local.entity.*
import kotlinx.coroutines.flow.Flow

class WeddingRepository(private val weddingDao: WeddingDao) {
    val user: Flow<UserEntity?> = weddingDao.getUser()
    val weddings: Flow<List<WeddingEntity>> = weddingDao.getWeddings()

    suspend fun insertUser(user: UserEntity) = weddingDao.insertUser(user)
    suspend fun insertWedding(wedding: WeddingEntity) = weddingDao.insertWedding(wedding)

    fun getBoards(weddingId: Long): Flow<List<BoardEntity>> = weddingDao.getBoards(weddingId)
    suspend fun insertBoard(board: BoardEntity) = weddingDao.insertBoard(board)

    fun getPins(boardId: Long): Flow<List<PinEntity>> = weddingDao.getPins(boardId)
    fun getPin(pinId: Long): Flow<PinEntity?> = weddingDao.getPin(pinId)
    suspend fun insertPin(pin: PinEntity) = weddingDao.insertPin(pin)
    suspend fun updatePin(pin: PinEntity) = weddingDao.updatePin(pin)
    suspend fun deletePin(pin: PinEntity) = weddingDao.deletePin(pin)
    suspend fun selectFinalPin(pinId: Long, boardId: Long) = weddingDao.selectFinalPin(pinId, boardId)

    fun getChecklist(weddingId: Long): Flow<List<ChecklistItemEntity>> = weddingDao.getChecklist(weddingId)
    suspend fun insertChecklistItem(item: ChecklistItemEntity) = weddingDao.insertChecklistItem(item)
    suspend fun updateChecklistItem(item: ChecklistItemEntity) = weddingDao.updateChecklistItem(item)

    fun getBudgetItems(weddingId: Long): Flow<List<BudgetItemEntity>> = weddingDao.getBudgetItems(weddingId)
    suspend fun insertBudgetItem(item: BudgetItemEntity) = weddingDao.insertBudgetItem(item)
}
