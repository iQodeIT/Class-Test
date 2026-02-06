package com.wedding.app.ui.screens.wedding

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.wedding.app.ui.viewmodel.WeddingViewModel

@Composable
fun CreateWeddingScreen(viewModel: WeddingViewModel, onComplete: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var date by remember { mutableStateOf(System.currentTimeMillis()) }
    var minBudget by remember { mutableStateOf("0") }
    var maxBudget by remember { mutableStateOf("10000") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Tell us about your Wedding",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(32.dp))
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Wedding Name (e.g. Sarah & Mark's Wedding)") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = location,
            onValueChange = { location = it },
            label = { Text("Location") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = minBudget,
                onValueChange = { minBudget = it },
                label = { Text("Min Budget") },
                modifier = Modifier.weight(1f)
            )
            OutlinedTextField(
                value = maxBudget,
                onValueChange = { maxBudget = it },
                label = { Text("Max Budget") },
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = {
                viewModel.addWedding(
                    name, date, location,
                    minBudget.toDoubleOrNull() ?: 0.0,
                    maxBudget.toDoubleOrNull() ?: 0.0
                )
                onComplete()
            },
            enabled = name.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Create Workspace")
        }
    }
}
