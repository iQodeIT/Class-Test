package com.apartment.planner.ui.screens.room

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.apartment.planner.data.local.entity.ChecklistItemEntity
import com.apartment.planner.ui.theme.LocalSpacing
import com.apartment.planner.ui.theme.Terracotta
import com.apartment.planner.ui.viewmodel.RoomUiState
import com.apartment.planner.ui.viewmodel.RoomViewModel

@Composable
fun ChecklistTab(uiState: RoomUiState, viewModel: RoomViewModel) {
    val spacing = LocalSpacing.current

    if (uiState.checklist.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Your checklist is empty.", style = MaterialTheme.typography.bodyLarge)
                Button(
                    onClick = { /* TODO: Add Template */ },
                    modifier = Modifier.padding(top = spacing.standard),
                    colors = ButtonDefaults.buttonColors(containerColor = Terracotta)
                ) {
                    Text("Load Room Template")
                }
            }
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(spacing.standard),
            verticalArrangement = Arrangement.spacedBy(spacing.compact)
        ) {
            items(uiState.checklist) { item ->
                ChecklistItemRow(item = item) {
                    viewModel.toggleChecklistItem(item)
                }
            }
        }
    }
}

@Composable
fun ChecklistItemRow(item: ChecklistItemEntity, onToggle: () -> Unit) {
    val spacing = LocalSpacing.current
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .padding(spacing.standard),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = item.isCompleted,
                onCheckedChange = { onToggle() },
                colors = CheckboxDefaults.colors(checkedColor = Terracotta)
            )
            Spacer(modifier = Modifier.width(spacing.compact))
            Text(
                text = item.title,
                style = MaterialTheme.typography.bodyLarge.copy(
                    textDecoration = if (item.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                ),
                color = if (item.isCompleted) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f) else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
