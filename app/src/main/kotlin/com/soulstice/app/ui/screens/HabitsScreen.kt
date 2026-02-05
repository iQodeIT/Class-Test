package com.soulstice.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.soulstice.app.ui.components.SoulsticeCard
import com.soulstice.app.ui.theme.*
import com.soulstice.app.ui.viewmodel.HabitViewModel

@Composable
fun HabitsScreen(
    viewModel: HabitViewModel = hiltViewModel()
) {
    val habitsWithStatus by viewModel.habitsWithStatus.collectAsState(initial = emptyList())
    var showAddHabit by remember { mutableStateOf(false) }

    if (showAddHabit) {
        AddHabitDialog(
            onDismiss = { showAddHabit = false },
            onAdd = { title, category ->
                viewModel.addHabit(title, category)
                showAddHabit = false
            }
        )
    }

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
            Text("Habit Library", style = MaterialTheme.typography.headlineLarge, color = Taupe900)
            IconButton(onClick = { showAddHabit = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add Habit", tint = Sage600)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (habitsWithStatus.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Your habit library is empty. Add your first habit!", color = Taupe500)
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                items(habitsWithStatus) { status ->
                    HabitLibraryItem(
                        name = status.habit.title,
                        isCompleted = status.isCompletedToday,
                        streak = status.streak,
                        onComplete = { viewModel.completeHabit(status.habit) }
                    )
                }
            }
        }
    }
}

@Composable
fun HabitLibraryItem(name: String, isCompleted: Boolean, streak: Int, onComplete: () -> Unit) {
    SoulsticeCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.Star,
                    contentDescription = null,
                    tint = if (isCompleted) Sage500 else Taupe100
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(name, color = Taupe900, fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold)
                    if (streak > 0) {
                        Text("🔥 $streak day streak", fontSize = 10.sp, color = Clay800)
                    }
                }
            }
            if (!isCompleted) {
                IconButton(onClick = onComplete) {
                    Icon(Icons.Default.Add, contentDescription = "Complete Habit", tint = Taupe500)
                }
            } else {
                Icon(Icons.Default.Star, contentDescription = "Completed", tint = Sage500)
            }
        }
    }
}

@Composable
fun AddHabitDialog(onDismiss: () -> Unit, onAdd: (String, String) -> Unit) {
    var title by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Well-Being") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("New Habit") },
        text = {
            Column {
                TextField(value = title, onValueChange = { title = it }, label = { Text("Habit Title") })
                // Simple category selector could be added here
            }
        },
        confirmButton = {
            Button(onClick = { if (title.isNotBlank()) onAdd(title, category) }) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
