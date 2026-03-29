package com.soulstice.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.soulstice.app.data.local.entities.InboxItem
import com.soulstice.app.ui.components.SoulsticeButton
import com.soulstice.app.ui.components.SoulsticeCard
import com.soulstice.app.ui.components.SoulsticeInput
import com.soulstice.app.ui.theme.*
import com.soulstice.app.ui.viewmodel.InboxViewModel

@Composable
fun InboxScreen(
    viewModel: InboxViewModel = hiltViewModel()
) {
    val items by viewModel.inboxItems.collectAsState(initial = emptyList())
    var newItem by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text("Inbox", style = MaterialTheme.typography.headlineLarge, color = Taupe900)
        Text("Quick capture holding pen.", color = Taupe500, fontSize = 14.sp)

        Spacer(modifier = Modifier.height(24.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            SoulsticeInput(
                value = newItem,
                onValueChange = { newItem = it },
                label = "Capture a thought...",
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            SoulsticeButton(text = "Add", onClick = {
                if (newItem.isNotBlank()) {
                    viewModel.addItem(newItem)
                    newItem = ""
                }
            })
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (items.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(64.dp), tint = Sage500.copy(alpha = 0.4f))
                    Text("Inbox Zero! Clear mind, clear focus.", color = Taupe500, style = MaterialTheme.typography.bodyLarge)
                }
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(items) { item ->
                    InboxProcessingCard(
                        item = item,
                        onToTask = { viewModel.processToTask(item, "task") },
                        onToIdea = { viewModel.processToTask(item, "idea") },
                        onArchive = { viewModel.archiveItem(item) }
                    )
                }
            }
        }
    }
}

@Composable
fun InboxProcessingCard(
    item: InboxItem,
    onToTask: () -> Unit,
    onToIdea: () -> Unit,
    onArchive: () -> Unit
) {
    SoulsticeCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(item.content, style = MaterialTheme.typography.titleMedium, color = Taupe900)
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = onToTask) {
                    Icon(Icons.Default.Assignment, contentDescription = "To Task", tint = Sage500)
                }
                IconButton(onClick = onToIdea) {
                    Icon(Icons.Default.Lightbulb, contentDescription = "To Idea", tint = Clay500)
                }
                IconButton(onClick = onArchive) {
                    Icon(Icons.Default.Archive, contentDescription = "Archive", tint = Taupe500)
                }
            }
        }
    }
}
