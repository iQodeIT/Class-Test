package com.soulstice.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.soulstice.app.ui.components.SoulsticeCard
import com.soulstice.app.ui.theme.*
import com.soulstice.app.ui.viewmodel.DashboardViewModel

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val energyLevel by viewModel.energyLevel.collectAsState()
    val tasks by viewModel.tasks.collectAsState(initial = emptyList())

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
            Column {
                Text(
                    text = "Monday, Oct 14",
                    style = MaterialTheme.typography.bodySmall,
                    color = Taupe500
                )
                Text(
                    text = "Good Morning",
                    style = MaterialTheme.typography.headlineLarge,
                    color = Taupe900
                )
            }
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(if (energyLevel == "high") Clay50 else Sage100)
                    .clickable { viewModel.toggleEnergy() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (energyLevel == "high") Icons.Default.Bolt else Icons.Default.BatteryChargingFull,
                    contentDescription = "Energy",
                    tint = if (energyLevel == "high") Clay500 else Sage500
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Metrics Section
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            SoulsticeCard(
                modifier = Modifier.weight(1f),
                containerColor = Sage600
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Global Progress", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
                    Text("64%", color = Color.White, style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = 0.64f,
                        modifier = Modifier.fillMaxWidth().height(4.dp).clip(CircleShape),
                        color = Color.White,
                        trackColor = Color.White.copy(alpha = 0.2f)
                    )
                }
            }
            SoulsticeCard(
                modifier = Modifier.weight(1f),
                containerColor = Taupe900
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("7-Day Velocity", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
                    Text("14 pts/avg", color = Color.White, style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    // Placeholder for a simple sparkline/velocity chart
                    Row(
                        modifier = Modifier.fillMaxWidth().height(20.dp),
                        horizontalArrangement = Arrangement.spacedBy(2.dp),
                        verticalAlignment = Alignment.Bottom
                    ) {
                        listOf(0.4f, 0.7f, 0.5f, 0.9f, 0.6f, 0.8f, 1f).forEach { height ->
                            Box(modifier = Modifier.weight(1f).fillMaxHeight(height).background(Color.White.copy(alpha = 0.6f)))
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Habits Tracker Section
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Daily Habits",
                        style = MaterialTheme.typography.titleLarge,
                        color = Taupe900
                    )
                    Text("🔥 12 day streak", color = Clay800, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    HabitIcon(icon = Icons.Default.WaterDrop, completed = true, label = "Hydrate")
                    HabitIcon(icon = Icons.Default.SelfImprovement, completed = true, label = "Meditation")
                    HabitIcon(icon = Icons.Default.MenuBook, completed = false, label = "Read")
                    HabitIcon(icon = Icons.Default.FitnessCenter, completed = false, label = "Workout")
                    HabitIcon(icon = Icons.Default.Lightbulb, completed = false, label = "Review")
                }
            }

            // Zone A: Execution
            item {
                Column {
                    Text(
                        text = "Today's Schedule",
                        style = MaterialTheme.typography.titleLarge,
                        color = Taupe900
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        ScheduleItem(time = "09:00", title = "Deep Work: UI Design", subtitle = "2 hours • ⚡️ High Energy", completed = true)
                        ScheduleItem(time = "11:30", title = "Team Sync", subtitle = "30 mins • 🔋 Low Energy", completed = false)
                    }
                }
            }

            item {
                Column {
                    Text(
                        text = "Active Tasks",
                        style = MaterialTheme.typography.titleLarge,
                        color = Taupe900
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        ActiveTaskItem(title = "Refactor Data Architecture", project = "Soulstice", energy = "High")
                        ActiveTaskItem(title = "Update Brand Guidelines", project = "Creative Studio", energy = "High")
                    }
                }
            }

            // Zone B: Incubation
            item {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Creative Incubator",
                            style = MaterialTheme.typography.titleMedium,
                            color = Taupe900
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        IncubatorItem("Podcast Idea")
                        IncubatorItem("New UI Kit")
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Business Leads",
                            style = MaterialTheme.typography.titleMedium,
                            color = Taupe900
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        LeadItem("Follow up: Acme Corp")
                        LeadItem("Draft Proposal")
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun ActiveTaskItem(title: String, project: String, energy: String) {
    SoulsticeCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(title, color = Taupe900, fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold)
                Text(project, color = Taupe500, fontSize = 12.sp)
            }
            Text(if (energy == "High") "⚡️" else "🔋", fontSize = 18.sp)
        }
    }
}

@Composable
fun IncubatorItem(title: String) {
    SoulsticeCard(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), containerColor = Sage50) {
        Text(title, modifier = Modifier.padding(12.dp), color = Sage700, fontSize = 12.sp)
    }
}

@Composable
fun LeadItem(title: String) {
    SoulsticeCard(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), containerColor = Clay50) {
        Text(title, modifier = Modifier.padding(12.dp), color = Clay800, fontSize = 12.sp)
    }
}

@Composable
fun HabitIcon(icon: androidx.compose.ui.graphics.vector.ImageVector, completed: Boolean, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(if (completed) Sage500 else Taupe100),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (completed) Color.White else Taupe500,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(label, fontSize = 10.sp, color = Taupe500)
    }
}

@Composable
fun ScheduleItem(time: String, title: String, subtitle: String, completed: Boolean) {
    SoulsticeCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(time, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, color = if (completed) Taupe900 else Taupe500, modifier = Modifier.width(48.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, color = Taupe900, fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold)
                Text(subtitle, color = Taupe500, fontSize = 12.sp)
            }
            if (completed) {
                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Sage500)
            } else {
                Box(modifier = Modifier.size(20.dp).clip(CircleShape).background(Taupe50).padding(1.dp).clip(CircleShape).background(Color.White))
            }
        }
    }
}
