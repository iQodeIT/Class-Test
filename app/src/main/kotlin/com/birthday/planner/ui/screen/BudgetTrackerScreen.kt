package com.birthday.planner.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.birthday.planner.ui.viewmodel.PartyViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BudgetTrackerScreen(
    viewModel: PartyViewModel,
    onBackClick: () -> Unit
) {
    val party by viewModel.selectedParty.collectAsState()
    val budgetItems by viewModel.budgetItems.collectAsState()
    val totalBudget = party?.budgetRange ?: 0f
    val totalSpent = budgetItems.sumOf { it.actualSpend.toDouble() }.toFloat()
    val remaining = totalBudget - totalSpent

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Budget Tracker") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { /* Export PDF Mock */ }) {
                        Icon(Icons.Default.Share, contentDescription = "Export")
                    }
                }
            )
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
                BudgetOverview(totalBudget, totalSpent, remaining)
            }

            items(budgetItems) { item ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(item.category.name)
                    Text("$${item.actualSpend} / $${item.estimatedBudget}", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun BudgetOverview(total: Float, spent: Float, remaining: Float) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Total Budget: $${total}", style = MaterialTheme.typography.titleMedium)
            Text(
                text = "$${spent}",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text("Spent So Far", style = MaterialTheme.typography.bodyMedium)

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Remaining: $${remaining}",
                style = MaterialTheme.typography.titleLarge,
                color = if (remaining >= 0) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.error
            )
        }
    }
}
