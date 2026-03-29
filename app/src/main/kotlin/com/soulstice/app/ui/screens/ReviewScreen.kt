package com.soulstice.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.soulstice.app.ui.components.SoulsticeButton
import com.soulstice.app.ui.components.SoulsticeCard
import androidx.compose.ui.unit.sp
import com.soulstice.app.ui.components.SoulsticeInput
import com.soulstice.app.ui.theme.*
import com.soulstice.app.ui.viewmodel.DashboardViewModel
import com.soulstice.app.ui.viewmodel.InboxViewModel
import com.soulstice.app.ui.viewmodel.JournalViewModel

@Composable
fun ReviewScreen(
    journalViewModel: JournalViewModel = hiltViewModel(),
    dashboardViewModel: DashboardViewModel = hiltViewModel(),
    inboxViewModel: InboxViewModel = hiltViewModel()
) {
    var step by remember { mutableIntStateOf(1) }
    var reflection by remember { mutableStateOf("") }
    val incompleteTasks by dashboardViewModel.allActiveTasks.collectAsState(initial = emptyList())
    val inboxItems by inboxViewModel.inboxItems.collectAsState(initial = emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        // Progress Bar
        LinearProgressIndicator(
            progress = step / 4f,
            modifier = Modifier.fillMaxWidth().height(4.dp).clip(CircleShape),
            color = Sage500,
            trackColor = Taupe100
        )
        Spacer(modifier = Modifier.height(32.dp))

        when (step) {
            1 -> {
                Text("1. Task Rollover", style = MaterialTheme.typography.headlineLarge, color = Taupe900)
                Text("${incompleteTasks.size} tasks were not completed. Move them to tomorrow?", color = Taupe500, modifier = Modifier.padding(top = 8.dp))

                Spacer(modifier = Modifier.height(32.dp))

                if (incompleteTasks.isEmpty()) {
                    Text("All tasks completed! Great job.", color = Sage500)
                } else {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(incompleteTasks) { task ->
                            RolloverTaskItem(task.title)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                SoulsticeButton(
                    text = if (incompleteTasks.isEmpty()) "Continue" else "Rollover All",
                    onClick = { step = 2 },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            2 -> {
                Text("2. Inbox Zero", style = MaterialTheme.typography.headlineLarge, color = Taupe900)
                Text("Clear your holding pen for a fresh start tomorrow.", color = Taupe500, modifier = Modifier.padding(top = 8.dp))

                Spacer(modifier = Modifier.height(32.dp))

                SoulsticeCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("📬 ${inboxItems.size} items in Inbox", style = MaterialTheme.typography.titleLarge, color = Taupe900)
                        if (inboxItems.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(16.dp))
                            Text("Process them in the Inbox screen.", color = Taupe500, fontSize = 14.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    SoulsticeButton(text = "Back", onClick = { step = 1 }, variant = "outline", modifier = Modifier.weight(1f))
                    SoulsticeButton(text = "Continue", onClick = { step = 3 }, modifier = Modifier.weight(1f))
                }
            }
            3 -> {
                Text("3. Daily Reflection", style = MaterialTheme.typography.headlineLarge, color = Taupe900)
                Text("What was your biggest win today?", color = Taupe500, modifier = Modifier.padding(top = 8.dp))

                Spacer(modifier = Modifier.height(32.dp))

                SoulsticeCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        SoulsticeInput(value = reflection, onValueChange = { reflection = it }, label = "Journal your win...")
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    SoulsticeButton(text = "Back", onClick = { step = 2 }, variant = "outline", modifier = Modifier.weight(1f))
                    SoulsticeButton(text = "Complete Shutdown", onClick = {
                        if (reflection.isNotBlank()) {
                            journalViewModel.addEntry(reflection, isWin = true)
                        }
                        step = 4
                    }, modifier = Modifier.weight(1f))
                }
            }
            4 -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .background(Sage100),
                        contentAlignment = Alignment.Center
                    ) {
                        ReviewIcon(Icons.Default.AutoAwesome, contentDescription = null, size = 64.dp, tint = Sage600)
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    Text("All Set!", style = MaterialTheme.typography.headlineLarge, color = Taupe900)
                    Text(
                        "You've cleared your mind. Enjoy your evening and see you tomorrow.",
                        color = Taupe500,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    SoulsticeButton(text = "Finish Review", onClick = { step = 1 }, modifier = Modifier.fillMaxWidth())
                }
            }
        }
    }
}

@Composable
private fun ReviewIcon(imageVector: androidx.compose.ui.graphics.vector.ImageVector, contentDescription: String?, size: androidx.compose.ui.unit.Dp, tint: androidx.compose.ui.graphics.Color) {
    androidx.compose.material3.Icon(imageVector, contentDescription, modifier = Modifier.size(size), tint = tint)
}

@Composable
fun RolloverTaskItem(title: String) {
    SoulsticeCard(modifier = Modifier.fillMaxWidth(), containerColor = Clay50) {
        Text(title, modifier = Modifier.padding(16.dp), color = Clay800)
    }
}
