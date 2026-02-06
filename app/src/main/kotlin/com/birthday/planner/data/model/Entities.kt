package com.birthday.planner.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.compose.ui.graphics.Color
import java.util.Date

@Entity(tableName = "parties")
data class Party(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val partyTitle: String,
    val birthdayPersonName: String,
    val age: Int,
    val partyDate: Date,
    val partyType: PartyType,
    val guestCount: Int,
    val budgetRange: Float
)

@Entity(
    tableName = "boards",
    foreignKeys = [
        ForeignKey(
            entity = Party::class,
            parentColumns = ["id"],
            childColumns = ["partyId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Board(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val partyId: Long,
    val boardName: String,
    val emoji: String,
    val gradient: String
)

@Entity(
    tableName = "pins",
    foreignKeys = [
        ForeignKey(
            entity = Board::class,
            parentColumns = ["id"],
            childColumns = ["boardId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Pin(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val boardId: Long,
    val imageUrl: String,
    val sourceLink: String?,
    val notes: String?,
    val estimatedCost: Float?,
    val quantity: Int?,
    val status: PinStatus,
    val isFavorite: Boolean,
    val createdDate: Date = Date()
)

@Entity(
    tableName = "checklist_items",
    foreignKeys = [
        ForeignKey(
            entity = Party::class,
            parentColumns = ["id"],
            childColumns = ["partyId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ChecklistItem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val partyId: Long,
    val title: String,
    val dueDate: Date,
    val isCompleted: Boolean,
    val linkedBoardId: Long?,
    val section: ChecklistSection,
    val priority: Int
)

@Entity(
    tableName = "budget_items",
    foreignKeys = [
        ForeignKey(
            entity = Party::class,
            parentColumns = ["id"],
            childColumns = ["partyId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class BudgetItem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val partyId: Long,
    val category: BudgetCategory,
    val estimatedBudget: Float,
    val actualSpend: Float,
    val notes: String?
)

data class ThemeSuggestion(
    val themeName: String,
    val colorPalette: List<String>, // Hex strings
    val suggestedBoards: List<String>,
    val emoji: String
)
