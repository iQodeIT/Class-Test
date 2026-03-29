package com.soulstice.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.soulstice.app.data.local.entities.Project
import com.soulstice.app.ui.components.SoulsticeButton
import com.soulstice.app.ui.components.SoulsticeCard
import com.soulstice.app.ui.components.SoulsticeProgressBar
import com.soulstice.app.ui.theme.*
import com.soulstice.app.ui.viewmodel.ProjectViewModel

@Composable
fun ProjectsScreen(
    onProjectClick: (String) -> Unit,
    viewModel: ProjectViewModel = hiltViewModel()
) {
    val projectsWithStats by viewModel.projectsWithStats.collectAsState(initial = emptyList())
    var showAddProject by remember { mutableStateOf(false) }

    if (showAddProject) {
        AddProjectDialog(
            onDismiss = { showAddProject = false },
            onAdd = { name, desc ->
                viewModel.addProject(name, desc, null)
                showAddProject = false
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
            Text("Projects", style = MaterialTheme.typography.headlineLarge, color = Taupe900)
            IconButton(onClick = { showAddProject = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add Project", tint = Sage600)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (projectsWithStats.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No projects yet. Start by defining a big goal!", color = Taupe500)
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                items(projectsWithStats) { stats ->
                    SoulsticeCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onProjectClick(stats.project.id) }
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(stats.project.name, style = MaterialTheme.typography.titleMedium, color = Taupe900)
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(Sage100)
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(stats.project.status.capitalize(), color = Sage700, fontSize = 10.sp)
                                }
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                            SoulsticeProgressBar(progress = stats.progress)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("${(stats.progress * 100).toInt()}% Complete", color = Taupe500, fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AddProjectDialog(onDismiss: () -> Unit, onAdd: (String, String) -> Unit) {
    var name by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("New Project") },
        text = {
            Column {
                TextField(value = name, onValueChange = { name = it }, label = { Text("Project Name") })
                Spacer(modifier = Modifier.height(8.dp))
                TextField(value = desc, onValueChange = { desc = it }, label = { Text("Description") })
            }
        },
        confirmButton = {
            Button(onClick = { if (name.isNotBlank()) onAdd(name, desc) }) {
                Text("Create")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
