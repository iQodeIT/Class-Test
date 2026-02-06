package com.wedding.app.ui.screens.budget

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import android.content.Intent
import com.wedding.app.data.local.entity.BudgetItemEntity
import com.wedding.app.ui.viewmodel.WeddingViewModel

@Composable
fun BudgetScreen(viewModel: WeddingViewModel) {
    val context = LocalContext.current
    val currentWedding by viewModel.currentWedding.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }

    if (showAddDialog && currentWedding != null) {
        AddBudgetDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { category, amount ->
                viewModel.addBudgetItem(currentWedding!!.id, category, amount)
                showAddDialog = false
            }
        )
    }

    currentWedding?.let { wedding ->
        val items by viewModel.getBudgetItems(wedding.id).collectAsState(initial = emptyList())

        Scaffold(
            floatingActionButton = {
                FloatingActionButton(onClick = { showAddDialog = true }) {
                    Icon(Icons.Default.Add, contentDescription = "Add Category")
                }
            }
        ) { padding ->
            LazyColumn(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Budget Planner",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    OutlinedButton(onClick = {
                        val summary = "Wedding Budget Summary: ${wedding.name}\n" +
                                     items.joinToString("\n") { "${it.category}: $${it.spentAmount} of $${it.allocatedAmount}" }
                        val sendIntent: Intent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, summary)
                            type = "text/plain"
                        }
                        val shareIntent = Intent.createChooser(sendIntent, null)
                        context.startActivity(shareIntent)
                    }) {
                        Text("Export Summary")
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
                BudgetDonutChart(items)
                Spacer(modifier = Modifier.height(32.dp))
            }

            items(items) { item ->
                BudgetCategoryCard(item)
                Spacer(modifier = Modifier.height(12.dp))
            }
            }
        }
    }
}

@Composable
fun AddBudgetDialog(onDismiss: () -> Unit, onConfirm: (String, Double) -> Unit) {
    var category by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Budget Category") },
        text = {
            Column {
                OutlinedTextField(value = category, onValueChange = { category = it }, label = { Text("Category (e.g. Flowers)") })
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = amount, onValueChange = { amount = it }, label = { Text("Allocated Amount") })
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(category, amount.toDoubleOrNull() ?: 0.0) }) { Text("Add") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}

@Composable
fun BudgetDonutChart(items: List<BudgetItemEntity>) {
    val totalSpent = items.sumOf { it.spentAmount }
    val totalBudget = items.sumOf { it.allocatedAmount }
    val progress = if (totalBudget > 0) (totalSpent / totalBudget).toFloat() else 0f

    Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.size(180.dp)) {
            drawArc(
                color = Color.LightGray.copy(alpha = 0.3f),
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = 20.dp.toPx(), cap = StrokeCap.Round)
            )
            drawArc(
                color = Color(0xFFB76E79), // Rose Gold
                startAngle = -90f,
                sweepAngle = 360f * progress,
                useCenter = false,
                style = Stroke(width = 20.dp.toPx(), cap = StrokeCap.Round)
            )
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "$${totalSpent.toInt()}", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Text(text = "of $${totalBudget.toInt()}", style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
fun BudgetCategoryCard(item: BudgetItemEntity) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = item.category, fontWeight = FontWeight.Bold)
                Text(text = "$${item.spentAmount.toInt()} / $${item.allocatedAmount.toInt()}")
            }
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(
                progress = { (item.spentAmount / item.allocatedAmount).toFloat().coerceIn(0f, 1f) },
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f)
            )
        }
    }
}
