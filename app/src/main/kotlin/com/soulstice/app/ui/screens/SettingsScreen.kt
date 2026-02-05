package com.soulstice.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.soulstice.app.ui.components.SoulsticeButton
import com.soulstice.app.ui.components.SoulsticeCard
import com.soulstice.app.ui.theme.*
import com.soulstice.app.ui.viewmodel.SettingsViewModel

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val pomodoroDuration by viewModel.pomodoroDuration.collectAsState()
    val theme by viewModel.theme.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text("Settings", style = MaterialTheme.typography.headlineLarge, color = Taupe900)
        Spacer(modifier = Modifier.height(24.dp))

        Text("Preferences", style = MaterialTheme.typography.titleLarge, color = Taupe900)
        Spacer(modifier = Modifier.height(16.dp))

        SoulsticeCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Theme", fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, color = Taupe900)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(selected = theme == "Light", onClick = { viewModel.setTheme("Light") })
                    Text("Light")
                    Spacer(modifier = Modifier.width(16.dp))
                    RadioButton(selected = theme == "Dark", onClick = { viewModel.setTheme("Dark") })
                    Text("Dark")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        SoulsticeCard(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Pomodoro Focus (minutes)", fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, color = Taupe900)
                Slider(
                    value = pomodoroDuration.toFloat(),
                    onValueChange = { viewModel.setPomodoroDuration(it.toInt()) },
                    valueRange = 5f..60f,
                    steps = 11
                )
                Text("$pomodoroDuration minutes", color = Taupe500)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text("Data Management", style = MaterialTheme.typography.titleLarge, color = Taupe900)
        Spacer(modifier = Modifier.height(16.dp))

        SoulsticeButton(
            text = "Export Data (JSON)",
            onClick = { /* Placeholder */ },
            variant = "outline",
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(12.dp))
        SoulsticeButton(
            text = "Import Data (JSON)",
            onClick = { /* Placeholder */ },
            variant = "outline",
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { viewModel.resetDatabase() },
            colors = ButtonDefaults.buttonColors(containerColor = Clay500),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Reset Database", color = androidx.compose.ui.graphics.Color.White)
        }
    }
}
