package com.birthday.planner.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.animation.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.birthday.planner.ui.component.ConfettiCelebration
import com.birthday.planner.ui.component.PinCard
import com.birthday.planner.ui.viewmodel.BoardViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DecisionModeScreen(
    viewModel: BoardViewModel,
    onBackClick: () -> Unit
) {
    val pins by viewModel.pins.collectAsState()
    val shortlistedPins = pins.filter { it.status.name == "SHORTLISTED" }

    var showCelebration by remember { mutableStateOf(false) }
    var selectedPinId by remember { mutableStateOf<Long?>(null) }
    val haptic = LocalHapticFeedback.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("⚡ Decision Mode ⚡", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Which one sparked the most joy?",
                    style = MaterialTheme.typography.headlineSmall,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(32.dp))

                if (shortlistedPins.size >= 2) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        shortlistedPins.take(2).forEach { pin ->
                            val isSelected = selectedPinId == pin.id
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .animateContentSize()
                            ) {
                                PinCard(
                                    pin = pin,
                                    onClick = { selectedPinId = pin.id },
                                    modifier = Modifier.alpha(if (selectedPinId == null || isSelected) 1f else 0.5f)
                                )
                                if (isSelected) {
                                    Box(
                                        modifier = Modifier
                                            .matchParentSize()
                                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), RoundedCornerShape(16.dp))
                                            .border(4.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(16.dp))
                                    )
                                }
                                Button(
                                    onClick = {
                                        selectedPinId = pin.id
                                        showCelebration = true
                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    },
                                    modifier = Modifier
                                        .align(Alignment.BottomCenter)
                                        .padding(8.dp),
                                    colors = if (isSelected) ButtonDefaults.buttonColors() else ButtonDefaults.filledTonalButtonColors()
                                ) {
                                    Text(if (isSelected) "Pitched! ✨" else "Pick This")
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))
                    Text("Comparison helps you avoid analysis paralysis!", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
                } else {
                    EmptyShortlistState()
                }
            }

            if (showCelebration) {
                ConfettiCelebration()
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.6f))
                        .clickable { showCelebration = false },
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        modifier = Modifier.padding(32.dp),
                        shape = RoundedCornerShape(32.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("🎉", fontSize = 64.sp)
                            Spacer(modifier = Modifier.height(16.dp))
                            Text("Great Choice!", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("This idea has been promoted to Final Choices.", textAlign = TextAlign.Center)
                            Spacer(modifier = Modifier.height(24.dp))
                            Button(onClick = { showCelebration = false }) {
                                Text("Awesome!")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyShortlistState() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text("No shortlisted ideas to compare.", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Go to your boards and shortlist some ideas first!", textAlign = TextAlign.Center)
    }
}
