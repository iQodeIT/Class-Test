package com.apartment.planner.ui.screens.room

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.apartment.planner.data.local.entity.BudgetItemEntity
import com.apartment.planner.ui.theme.LocalSpacing
import com.apartment.planner.ui.theme.Terracotta
import com.apartment.planner.ui.viewmodel.RoomUiState
import com.apartment.planner.ui.viewmodel.RoomViewModel

@Composable
fun BudgetTab(uiState: RoomUiState, viewModel: RoomViewModel) {
    val spacing = LocalSpacing.current
    val totalPlanned = uiState.budget.sumOf { it.estimatedCost }
    val totalSpent = uiState.budget.sumOf { it.actualCost ?: 0.0 }

    Column(modifier = Modifier.fillMaxSize()) {
        // Budget Overview Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(spacing.standard),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f))
        ) {
            Column(modifier = Modifier.padding(spacing.standard)) {
                Text("Room Budget Overview", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(spacing.compact))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Planned: $${"%.2f".format(totalPlanned)}")
                    Text("Spent: $${"%.2f".format(totalSpent)}", fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(spacing.compact))
                LinearProgressIndicator(
                    progress = if (totalPlanned > 0) (totalSpent / totalPlanned).toFloat().coerceIn(0f, 1f) else 0f,
                    modifier = Modifier.fillMaxWidth().height(8.dp),
                    color = if (totalSpent > totalPlanned) Color.Red else Terracotta
                )
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(spacing.standard),
            verticalArrangement = Arrangement.spacedBy(spacing.compact)
        ) {
            items(uiState.budget) { item ->
                BudgetItemRow(item = item)
            }
        }
    }
}

@Composable
fun BudgetItemRow(item: BudgetItemEntity) {
    val spacing = LocalSpacing.current
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp
    ) {
        Row(
            modifier = Modifier.padding(spacing.standard),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = item.name, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
                Text(text = item.category, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
            }
            Text(
                text = "$${"%.2f".format(item.actualCost ?: item.estimatedCost)}",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = if (item.status == "BOUGHT") Color(0xFF2E7D32) else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
