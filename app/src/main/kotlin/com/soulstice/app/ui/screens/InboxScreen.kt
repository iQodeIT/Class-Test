package com.soulstice.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.soulstice.app.ui.components.SoulsticeButton
import com.soulstice.app.ui.components.SoulsticeCard
import com.soulstice.app.ui.components.SoulsticeInput
import com.soulstice.app.ui.theme.Taupe500
import com.soulstice.app.ui.theme.Taupe900

@Composable
fun InboxScreen() {
    var newItem by remember { mutableStateOf("") }
    val items = remember { mutableStateListOf<String>() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text("Inbox", style = MaterialTheme.typography.headlineLarge, color = Taupe900)

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
                    items.add(0, newItem)
                    newItem = ""
                }
            })
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (items.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.Inbox, contentDescription = null, modifier = Modifier.size(64.dp), tint = Taupe500.copy(alpha = 0.4f))
                    Text("Your inbox is empty", color = Taupe500, style = MaterialTheme.typography.bodyLarge)
                }
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(items) { item ->
                    SoulsticeCard(modifier = Modifier.fillMaxWidth()) {
                        Text(item, modifier = Modifier.padding(16.dp), color = Taupe900)
                    }
                }
            }
        }
    }
}
