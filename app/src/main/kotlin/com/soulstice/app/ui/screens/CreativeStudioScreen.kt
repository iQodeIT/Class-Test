package com.soulstice.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Image
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
fun CreativeStudioScreen(
    viewModel: ResourceViewModel = hiltViewModel()
) {
    val resources by viewModel.allResources.collectAsState(initial = emptyList())
    val images = resources.filter { it.type == "image" }
    var showAddImage by remember { mutableStateOf(false) }

    if (showAddImage) {
        AddImageDialog(
            onDismiss = { showAddImage = false },
            onAdd = { title, uri ->
                viewModel.addResource(title, "image", localUri = uri)
                showAddImage = false
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
            Text("Creative Studio", style = MaterialTheme.typography.headlineLarge, color = Taupe900)
            IconButton(onClick = { showAddImage = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add Image", tint = Sage600)
            }
        }
        Text("Mood boards and visual inspirations.", color = Taupe500, fontSize = 14.sp)

        Spacer(modifier = Modifier.height(24.dp))

        if (images.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No visual inspirations yet.", color = Taupe500)
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(images) { image ->
                    SoulsticeCard(modifier = Modifier.aspectRatio(1f)) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(Icons.Default.Image, contentDescription = null, tint = Taupe100, modifier = Modifier.size(48.dp))
                                Text(image.title, color = Taupe500, fontSize = 10.sp, modifier = Modifier.padding(8.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AddImageDialog(onDismiss: () -> Unit, onAdd: (String, String) -> Unit) {
    var title by remember { mutableStateOf("") }
    var uri by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Inspiration") },
        text = {
            Column {
                TextField(value = title, onValueChange = { title = it }, label = { Text("Title") })
                Spacer(modifier = Modifier.height(8.dp))
                TextField(value = uri, onValueChange = { uri = it }, label = { Text("Image URI / Path") })
            }
        },
        confirmButton = {
            Button(onClick = { if (title.isNotBlank()) onAdd(title, uri) }) {
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
