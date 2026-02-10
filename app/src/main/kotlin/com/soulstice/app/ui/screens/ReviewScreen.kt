package com.soulstice.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import com.soulstice.app.ui.components.SoulsticeButton
import com.soulstice.app.ui.components.SoulsticeCard
import com.soulstice.app.ui.components.SoulsticeInput
import com.soulstice.app.ui.theme.*

@Composable
fun ReviewScreen() {
    var step by remember { mutableIntStateOf(1) }
    var reflection by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        when (step) {
            1 -> {
                Text("Daily Shutdown", style = MaterialTheme.typography.headlineLarge, color = Taupe900)
                Text("Let's reflect on your day and clear your mind for tomorrow.", color = Taupe500, modifier = Modifier.padding(top = 8.dp))

                Spacer(modifier = Modifier.height(32.dp))

                SoulsticeCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("What went well today?", color = Taupe900, fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold)
                        Spacer(modifier = Modifier.height(8.dp))
                        SoulsticeInput(value = reflection, onValueChange = { reflection = it }, label = "Write your highlights...")
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                SoulsticeButton(text = "Next Step", onClick = { step = 2 }, modifier = Modifier.fillMaxWidth())
            }
            2 -> {
                Text("Task Rollover", style = MaterialTheme.typography.headlineLarge, color = Taupe900)
                Text("3 tasks were not completed. Move them to tomorrow?", color = Taupe500, modifier = Modifier.padding(top = 8.dp))

                Spacer(modifier = Modifier.height(32.dp))

                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    RolloverTaskItem("Refactor Soulstice DAL")
                    RolloverTaskItem("Write documentation")
                    RolloverTaskItem("Call client for feedback")
                }

                Spacer(modifier = Modifier.height(32.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    SoulsticeButton(text = "Back", onClick = { step = 1 }, variant = "outline", modifier = Modifier.weight(1f))
                    SoulsticeButton(text = "Rollover All", onClick = { step = 3 }, modifier = Modifier.weight(1f))
                }
            }
            3 -> {
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
