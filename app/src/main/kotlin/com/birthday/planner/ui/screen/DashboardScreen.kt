package com.birthday.planner.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.birthday.planner.ui.component.PartyCard
import com.birthday.planner.ui.viewmodel.PartyViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: PartyViewModel,
    onPartyClick: (Long) -> Unit,
    onAddPartyClick: () -> Unit,
    onGenerateThemeClick: () -> Unit
) {
    val parties by viewModel.allParties.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("My Parties 🎉", fontWeight = FontWeight.Bold)
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddPartyClick) {
                Icon(Icons.Default.Add, contentDescription = "Add Party")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                GeneratorEntryCard(onGenerateThemeClick)
            }
            items(parties) { party ->
                PartyCard(
                    party = party,
                    onClick = { onPartyClick(party.id) }
                )
            }
            if (parties.isEmpty()) {
                item {
                    EmptyPartiesView()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GeneratorEntryCard(onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.padding(24.dp),
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
        ) {
            Text("✨", fontSize = 32.sp)
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text("Need Inspiration?", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text("Try our Theme Generator!", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable
fun EmptyPartiesView() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
    ) {
        Text("No parties yet!", style = MaterialTheme.typography.headlineSmall)
        Text(
            "Tap the + button to start planning your first celebration.",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}
