package com.soulstice.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.soulstice.app.ui.components.SoulsticeCard
import com.soulstice.app.ui.theme.*
import com.soulstice.app.ui.viewmodel.DashboardViewModel

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val energyLevel by viewModel.energyLevel.collectAsState()
    val tasks by viewModel.tasks.collectAsState(initial = emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Monday, Oct 14",
                    style = MaterialTheme.typography.bodySmall,
                    color = Taupe500
                )
                Text(
                    text = "Good Morning",
                    style = MaterialTheme.typography.headlineLarge,
                    color = Taupe900
                )
            }
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(if (energyLevel == "high") Clay50 else Sage100)
                    .clickable { viewModel.toggleEnergy() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (energyLevel == "high") Icons.Default.Bolt else Icons.Default.BatteryChargingFull,
                    contentDescription = "Energy",
                    tint = if (energyLevel == "high") Clay500 else Sage500
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            SoulsticeCard(
                modifier = Modifier.weight(1f),
                containerColor = Sage600
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Daily Goal", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
                    Text("85% Done", color = Color.White, style = MaterialTheme.typography.titleMedium)
                }
            }
            SoulsticeCard(
                modifier = Modifier.weight(1f),
                containerColor = Taupe900
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Velocity", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
                    Text("12 pts/day", color = Color.White, style = MaterialTheme.typography.titleMedium)
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Today's Schedule",
            style = MaterialTheme.typography.headlineSmall,
            color = Taupe900
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            item {
                ScheduleItem(time = "09:00", title = "Deep Work: UI Design", subtitle = "2 hours • High Energy", completed = true)
            }
            item {
                ScheduleItem(time = "11:30", title = "Team Sync", subtitle = "30 mins • Low Energy", completed = false)
            }
        }
    }
}

@Composable
fun ScheduleItem(time: String, title: String, subtitle: String, completed: Boolean) {
    SoulsticeCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(time, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold, color = if (completed) Taupe900 else Taupe500, modifier = Modifier.width(48.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, color = Taupe900, fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold)
                Text(subtitle, color = Taupe500, fontSize = 12.sp)
            }
            if (completed) {
                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Sage500)
            } else {
                Box(modifier = Modifier.size(20.dp).clip(CircleShape).background(Taupe50).padding(1.dp).clip(CircleShape).background(Color.White))
            }
        }
    }
}
