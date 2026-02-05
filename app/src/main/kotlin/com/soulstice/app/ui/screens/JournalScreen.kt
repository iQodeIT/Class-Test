package com.soulstice.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.soulstice.app.ui.components.SoulsticeButton
import com.soulstice.app.ui.components.SoulsticeCard
import com.soulstice.app.ui.components.SoulsticeInput
import com.soulstice.app.ui.theme.*
import com.soulstice.app.ui.viewmodel.JournalViewModel
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun JournalScreen(
    viewModel: JournalViewModel = hiltViewModel()
) {
    val entries by viewModel.allEntries.collectAsState(initial = emptyList())
    var showAddEntry by remember { mutableStateOf(false) }
    var newContent by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Daily Journal", style = MaterialTheme.typography.headlineLarge, color = Taupe900)
            IconButton(onClick = { showAddEntry = true }) {
                Icon(Icons.Default.Add, contentDescription = "New Entry", tint = Sage600)
            }
        }
        Text("Reflection logs and win tracking.", color = Taupe500, fontSize = 14.sp)

        Spacer(modifier = Modifier.height(24.dp))

        if (showAddEntry) {
            SoulsticeCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    SoulsticeInput(
                        value = newContent,
                        onValueChange = { newContent = it },
                        label = "How was your day?"
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                        TextButton(onClick = { showAddEntry = false }) { Text("Cancel") }
                        Button(onClick = {
                            if (newContent.isNotBlank()) {
                                viewModel.addEntry(newContent)
                                newContent = ""
                                showAddEntry = false
                            }
                        }) { Text("Save Entry") }
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        if (entries.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No journal entries yet. Start reflecting!", color = Taupe500)
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                items(entries) { entry ->
                    val dateStr = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(entry.date)
                    SoulsticeCard(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(dateStr, color = Taupe500, fontSize = 12.sp)
                                if (entry.isWin) {
                                    Text("🏆 Big Win", color = Clay500, fontSize = 12.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
                                }
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(entry.content, color = Taupe900)
                        }
                    }
                }
            }
        }
    }
}
