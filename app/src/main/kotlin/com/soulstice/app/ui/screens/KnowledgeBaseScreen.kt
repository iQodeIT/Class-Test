package com.soulstice.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Link
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.soulstice.app.ui.components.SoulsticeCard
import com.soulstice.app.ui.theme.*
import com.soulstice.app.ui.viewmodel.ResourceViewModel

@Composable
fun KnowledgeBaseScreen(
    viewModel: ResourceViewModel = hiltViewModel()
) {
    val resources by viewModel.allResources.collectAsState(initial = emptyList())
    var showAddResource by remember { mutableStateOf(false) }

    if (showAddResource) {
        AddResourceDialog(
            onDismiss = { showAddResource = false },
            onAdd = { title, type, content, url ->
                viewModel.addResource(title, type, content, url)
                showAddResource = false
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
            Text("Knowledge Base", style = MaterialTheme.typography.headlineLarge, color = Taupe900)
            IconButton(onClick = { showAddResource = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add Resource", tint = Sage600)
            }
        }
        Text("Information repository: Notes, Links, and Documents.", color = Taupe500, fontSize = 14.sp)

        Spacer(modifier = Modifier.height(24.dp))

        if (resources.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No resources found. Capture some wisdom!", color = Taupe500)
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                items(resources.filter { it.type != "image" }) { resource ->
                    SoulsticeCard(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (resource.type == "link") Icons.Default.Link else Icons.Default.Description,
                                contentDescription = null,
                                tint = Sage500
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text(resource.title, style = MaterialTheme.typography.titleMedium, color = Taupe900)
                                if (!resource.content.isNullOrBlank()) {
                                    Text(resource.content, color = Taupe500, fontSize = 12.sp, maxLines = 2)
                                }
                                if (!resource.url.isNullOrBlank()) {
                                    Text(resource.url, color = Sage600, fontSize = 12.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AddResourceDialog(onDismiss: () -> Unit, onAdd: (String, String, String?, String?) -> Unit) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var url by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("note") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("New Resource") },
        text = {
            Column {
                TextField(value = title, onValueChange = { title = it }, label = { Text("Title") })
                Spacer(modifier = Modifier.height(8.dp))
                Row {
                    RadioButton(selected = type == "note", onClick = { type = "note" })
                    Text("Note", modifier = Modifier.align(Alignment.CenterVertically))
                    Spacer(modifier = Modifier.width(16.dp))
                    RadioButton(selected = type == "link", onClick = { type = "link" })
                    Text("Link", modifier = Modifier.align(Alignment.CenterVertically))
                }
                if (type == "note") {
                    TextField(value = content, onValueChange = { content = it }, label = { Text("Content") })
                } else {
                    TextField(value = url, onValueChange = { url = it }, label = { Text("URL") })
                }
            }
        },
        confirmButton = {
            Button(onClick = { if (title.isNotBlank()) onAdd(title, type, content, url) }) {
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
