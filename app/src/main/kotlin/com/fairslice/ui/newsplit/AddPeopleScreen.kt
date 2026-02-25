package com.fairslice.ui.newsplit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fairslice.logic.IncomeType
import com.fairslice.ui.components.BarData
import com.fairslice.ui.components.GlassCard
import com.fairslice.ui.components.ProportionalBarChart
import com.fairslice.ui.theme.*

@Composable
fun AddPeopleScreen(
    onResults: (Long) -> Unit,
    viewModel: NewSplitViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    // Auto-navigate to results when savedBillId is set
    LaunchedEffect(uiState.savedBillId) {
        uiState.savedBillId?.let { onResults(it) }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDeep)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            Text(
                text = "Who's splitting?",
                style = MaterialTheme.typography.headlineLarge,
                color = Color.White
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "${uiState.billName} • $${uiState.totalAmount}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = AmberHighlight
                    )
                }

                IconButton(onClick = { viewModel.toggleAnonymity() }) {
                    Icon(
                        if (uiState.isAnonymized) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = "Anonymity Mode",
                        tint = if (uiState.isAnonymized) AmberHighlight else Color.White.copy(alpha = 0.5f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 100.dp)
            ) {
                items(uiState.participants) { participant ->
                    ParticipantCard(
                        participant = participant,
                        isAnonymized = uiState.isAnonymized,
                        onUpdate = { n, i, t -> viewModel.updateParticipant(participant.id, n, i, t) },
                        onRemove = { viewModel.removeParticipant(participant.id) }
                    )
                }

                item {
                    TextButton(
                        onClick = { viewModel.addParticipant() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, tint = AmberHighlight)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Add another person", color = AmberHighlight)
                    }
                }
            }
        }

        // Animated Bar Chart at the bottom
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(Color.Black.copy(alpha = 0.5f))
                .padding(24.dp)
        ) {
            val bars = uiState.participants.mapIndexed { index, p ->
                val income = p.income.toDoubleOrNull() ?: 0.0
                val color = when (index % 5) {
                    0 -> IndigoPrimary
                    1 -> VioletSecondary
                    2 -> AmberHighlight
                    3 -> SuccessGreen
                    else -> Color.Cyan
                }
                BarData(income.toFloat(), color)
            }

            Text(
                text = "Live Breakdown",
                style = MaterialTheme.typography.labelMedium,
                color = Color.White.copy(alpha = 0.7f),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            ProportionalBarChart(bars = bars)

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { viewModel.saveSplit() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AmberHighlight),
                shape = RoundedCornerShape(16.dp),
                enabled = !uiState.isSaving && uiState.participants.all { it.name.isNotEmpty() && it.income.isNotEmpty() }
            ) {
                if (uiState.isSaving) {
                    CircularProgressIndicator(color = IndigoPrimary, modifier = Modifier.size(24.dp))
                } else {
                    Text("Calculate Fair Split ⚖️", color = IndigoPrimary, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun ParticipantCard(
    participant: ParticipantUi,
    isAnonymized: Boolean,
    onUpdate: (String?, String?, IncomeType?) -> Unit,
    onRemove: () -> Unit
) {
    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(VioletSecondary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (participant.name.isNotEmpty()) participant.name.take(1).uppercase() else "?",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                TextField(
                    value = participant.name,
                    onValueChange = { onUpdate(it, null, null) },
                    placeholder = { Text("Name", color = Color.Gray) },
                    modifier = Modifier.weight(1f),
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.White.copy(alpha = 0.3f),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    singleLine = true
                )

                IconButton(onClick = onRemove) {
                    Icon(Icons.Default.Delete, contentDescription = "Remove", tint = ErrorRed)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                TextField(
                    value = if (isAnonymized && participant.income.isNotEmpty()) "****" else participant.income,
                    onValueChange = { if (!isAnonymized || it.isEmpty()) onUpdate(null, it, null) },
                    placeholder = { Text("Income", color = Color.Gray) },
                    modifier = Modifier.weight(1f),
                    prefix = { Text("$ ", color = AmberHighlight) },
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.White.copy(alpha = 0.3f),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    singleLine = true
                )

                Spacer(modifier = Modifier.width(8.dp))

                var expanded by remember { mutableStateOf(false) }
                Box {
                    TextButton(onClick = { expanded = true }) {
                        Text(participant.incomeType.name.lowercase().replaceFirstChar { it.uppercase() }, color = AmberHighlight)
                    }
                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        IncomeType.entries.forEach { type ->
                            DropdownMenuItem(
                                text = { Text(type.name.lowercase().replaceFirstChar { it.uppercase() }) },
                                onClick = {
                                    onUpdate(null, null, type)
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
