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
import com.soulstice.app.data.local.entities.Habit
import com.soulstice.app.ui.components.SoulsticeCard
import com.soulstice.app.ui.theme.*
import com.soulstice.app.ui.viewmodel.DashboardViewModel
import com.soulstice.app.ui.viewmodel.HabitViewModel

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = hiltViewModel(),
    habitViewModel: HabitViewModel = hiltViewModel()
) {
    val energyLevel by viewModel.energyLevel.collectAsState(initial = "high")
    val activeTasks by viewModel.activeTasks.collectAsState(initial = emptyList())
    val todayTasks by viewModel.todayTasks.collectAsState(initial = emptyList())
    val incubatorIdeas by viewModel.incubatorIdeas.collectAsState(initial = emptyList())
    val businessLeads by viewModel.businessLeads.collectAsState(initial = emptyList())
    val globalProgress by viewModel.globalProgress.collectAsState(initial = 0f)
    val velocity by viewModel.velocity.collectAsState(initial = emptyList())
    val averageVelocity by viewModel.averageVelocity.collectAsState(initial = 0f)
    val habitsWithStatus by habitViewModel.habitsWithStatus.collectAsState(initial = emptyList())

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
                    Text("${(globalProgress * 100).toInt()}%", color = Color.White, style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = globalProgress,
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
                    Text("${String.format("%.1f", averageVelocity)} pts/day", color = Color.White, style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    // Bars based on real velocity data
                    Row(
                        modifier = Modifier.fillMaxWidth().height(20.dp),
                        horizontalArrangement = Arrangement.spacedBy(2.dp),
                        verticalAlignment = Alignment.Bottom
                    ) {
                        val max = (velocity.maxOrNull() ?: 1).toFloat()
                        velocity.forEach { count ->
                            Box(modifier = Modifier.weight(1f).fillMaxHeight(if (max > 0) count / max else 0.1f).background(Color.White.copy(alpha = 0.6f)))
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
                val maxStreak = if (habitsWithStatus.isEmpty()) 0 else habitsWithStatus.maxOf { it.streak }
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
                    if (maxStreak > 1) {
                        Text("🔥 $maxStreak day streak", color = Clay800, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                if (habitsWithStatus.isEmpty()) {
                    Text("No habits tracked. Add some in Habits library.", color = Taupe500, fontSize = 14.sp)
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        habitsWithStatus.take(5).forEach { status ->
                            HabitIcon(
                                icon = Icons.Default.Star, // Default icon
                                completed = status.isCompletedToday,
                                label = status.habit.title,
                                onClick = { habitViewModel.completeHabit(status.habit) }
                            )
                        }
                    }
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
                    if (todayTasks.isEmpty()) {
                        Text("No tasks scheduled for today.", color = Taupe500, fontSize = 14.sp)
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            todayTasks.forEach { task ->
                                ScheduleItem(
                                    time = "Today",
                                    title = task.title,
                                    subtitle = "${task.priority.capitalize()} • ${if (task.energy == "high") "⚡️" else "🔋"}",
                                    completed = task.status == "done",
                                    onClick = { viewModel.completeTask(task) }
                                )
                            }
                        }
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
                    if (activeTasks.isEmpty()) {
                        Text("No active tasks.", color = Taupe500, fontSize = 14.sp)
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            activeTasks.forEach { task ->
                                ActiveTaskItem(
                                    title = task.title,
                                    project = task.projectId ?: "No Project",
                                    energy = task.energy.capitalize(),
                                    onClick = { viewModel.completeTask(task) }
                                )
                            }
                        }
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
                        if (incubatorIdeas.isEmpty()) {
                            Text("No ideas yet.", color = Taupe500, fontSize = 12.sp)
                        } else {
                            incubatorIdeas.forEach { idea ->
                                IncubatorItem(idea.title)
                            }
                        }
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Business Leads",
                            style = MaterialTheme.typography.titleMedium,
                            color = Taupe900
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        if (businessLeads.isEmpty()) {
                            Text("No leads yet.", color = Taupe500, fontSize = 12.sp)
                        } else {
                            businessLeads.forEach { lead ->
                                LeadItem(lead.name)
                            }
                        }
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
fun ActiveTaskItem(title: String, project: String, energy: String, onClick: () -> Unit = {}) {
    SoulsticeCard(
        modifier = Modifier.fillMaxWidth().clickable { onClick() }
    ) {
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
fun HabitIcon(icon: androidx.compose.ui.graphics.vector.ImageVector, completed: Boolean, label: String, onClick: () -> Unit = {}) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(if (completed) Sage500 else Taupe100)
                .clickable { onClick() },
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
fun ScheduleItem(time: String, title: String, subtitle: String, completed: Boolean, onClick: () -> Unit = {}) {
    SoulsticeCard(
        modifier = Modifier.fillMaxWidth().clickable { onClick() }
    ) {
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
