package com.soulstice.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.soulstice.app.data.local.entities.Task
import com.soulstice.app.ui.components.SoulsticeCard
import com.soulstice.app.ui.theme.*
import com.soulstice.app.ui.viewmodel.ProjectViewModel

@Composable
fun ProjectDetailScreen(
    projectId: String,
    viewModel: ProjectViewModel = hiltViewModel()
) {
    val statsList by viewModel.projectsWithStats.collectAsState(initial = emptyList())
    val stats = statsList.find { it.project.id == projectId }

    if (stats == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = Sage500)
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(stats.project.name, style = MaterialTheme.typography.headlineMedium, color = Taupe900)
                Text(stats.project.description ?: "", color = Taupe500, fontSize = 14.sp)
            }
            ProjectProgressRing(progress = stats.progress)
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text("Kanban Board", style = MaterialTheme.typography.titleLarge, color = Taupe900)
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth().weight(1f),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            KanbanColumn("To Do", stats.tasks.filter { it.status == "todo" }, modifier = Modifier.weight(1f)) { task ->
                viewModel.updateTaskStatus(task, "in_progress")
            }
            KanbanColumn("In Progress", stats.tasks.filter { it.status == "in_progress" }, modifier = Modifier.weight(1f)) { task ->
                viewModel.updateTaskStatus(task, "done")
            }
            KanbanColumn("Done", stats.tasks.filter { it.status == "done" }, modifier = Modifier.weight(1f)) { task ->
                viewModel.updateTaskStatus(task, "todo")
            }
        }
    }
}

@Composable
fun ProjectProgressRing(progress: Float) {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(64.dp)) {
        androidx.compose.foundation.Canvas(modifier = Modifier.size(64.dp)) {
            drawCircle(
                color = Taupe100,
                style = Stroke(width = 6.dp.toPx())
            )
            drawArc(
                color = Sage500,
                startAngle = -90f,
                sweepAngle = 360f * progress,
                useCenter = false,
                style = Stroke(width = 6.dp.toPx(), cap = StrokeCap.Round)
            )
        }
        Text("${(progress * 100).toInt()}%", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Taupe900)
    }
}

@Composable
fun KanbanColumn(title: String, tasks: List<Task>, modifier: Modifier = Modifier, onTaskClick: (Task) -> Unit) {
    Column(modifier = modifier) {
        Text(title, style = MaterialTheme.typography.titleSmall, color = Taupe500)
        Spacer(modifier = Modifier.height(8.dp))
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Taupe50, RoundedCornerShape(8.dp))
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(tasks) { task ->
                SoulsticeCard(
                    modifier = Modifier.fillMaxWidth().clickable { onTaskClick(task) }
                ) {
                    Text(task.title, modifier = Modifier.padding(12.dp), fontSize = 12.sp, color = Taupe900)
                }
            }
        }
    }
}
