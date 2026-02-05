package com.soulstice.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.soulstice.app.ui.components.SoulsticeCard
import com.soulstice.app.ui.theme.*

@Composable
fun HabitsScreen() {
    val categories = listOf(
        HabitCategory("Founder's Core", listOf("Deep Work", "Strategic Planning", "Networking")),
        HabitCategory("Creator's Flow", listOf("Daily Writing", "Sketching", "Inspiration Search")),
        HabitCategory("Well-Being", listOf("Meditation", "Hydration", "Daily Walk"))
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text("Habit Library", style = MaterialTheme.typography.headlineLarge, color = Taupe900)
        Spacer(modifier = Modifier.height(24.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(24.dp)) {
            items(categories) { category ->
                Column {
                    Text(
                        text = category.name,
                        style = MaterialTheme.typography.titleLarge,
                        color = Taupe900
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    category.habits.forEach { habit ->
                        HabitLibraryItem(habit)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun HabitLibraryItem(name: String) {
    SoulsticeCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Star, contentDescription = null, tint = Sage500)
                Spacer(modifier = Modifier.width(12.dp))
                Text(name, color = Taupe900, fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold)
            }
            IconButton(onClick = { }) {
                Icon(Icons.Default.Add, contentDescription = "Add Habit", tint = Taupe500)
            }
        }
    }
}

data class HabitCategory(val name: String, val habits: List<String>)
