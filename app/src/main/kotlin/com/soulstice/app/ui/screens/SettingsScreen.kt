package com.soulstice.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.soulstice.app.ui.theme.Taupe900

@Composable
fun SettingsScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text("Settings", style = MaterialTheme.typography.headlineLarge, color = Taupe900)
        Spacer(modifier = Modifier.height(16.dp))
        Text("Account, Theme, Timer settings, and Data Export/Import.")
    }
}
