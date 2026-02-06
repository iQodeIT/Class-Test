package com.wedding.app.ui.screens.checklist

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.wedding.app.data.local.entity.ChecklistItemEntity
import com.wedding.app.ui.viewmodel.WeddingViewModel

@Composable
fun ChecklistScreen(viewModel: WeddingViewModel) {
    val currentWedding by viewModel.currentWedding.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }

    if (showAddDialog && currentWedding != null) {
        AddTaskDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { title, phase ->
                viewModel.addChecklistItem(currentWedding!!.id, title, phase)
                showAddDialog = false
            }
        )
    }

    currentWedding?.let { wedding ->
        val items by viewModel.getChecklist(wedding.id).collectAsState(initial = emptyList())
        val groupedItems = items.groupBy { it.phase }

        Scaffold(
            floatingActionButton = {
                FloatingActionButton(onClick = { showAddDialog = true }) {
                    Icon(Icons.Default.Add, contentDescription = "Add Task")
                }
            }
        ) { padding ->
            LazyColumn(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            item {
                Text(
                    text = "Wedding Checklist",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            groupedItems.forEach { (phase, phaseItems) ->
                item {
                    Text(
                        text = phase,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(vertical = 8.dp),
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
                items(phaseItems) { item ->
                    ChecklistRow(item) {
                        viewModel.toggleChecklistItem(item)
                    }
                }
            }
            }
        }
    }
}

@Composable
fun AddTaskDialog(onDismiss: () -> Unit, onConfirm: (String, String) -> Unit) {
    var title by remember { mutableStateOf("") }
    var phase by remember { mutableStateOf("12 Months Out") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add New Task") },
        text = {
            Column {
                OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Task Title") })
                Spacer(modifier = Modifier.height(8.dp))
                // For simplicity, using a TextField for phase
                OutlinedTextField(value = phase, onValueChange = { phase = it }, label = { Text("Phase (e.g. 12 Months Out)") })
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(title, phase) }) { Text("Add") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
fun ChecklistRow(item: ChecklistItemEntity, onToggle: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(checked = item.isCompleted, onCheckedChange = { onToggle() })
        Text(
            text = item.title,
            style = MaterialTheme.typography.bodyLarge,
            textDecoration = if (item.isCompleted) TextDecoration.LineThrough else null
        )
    }
}
