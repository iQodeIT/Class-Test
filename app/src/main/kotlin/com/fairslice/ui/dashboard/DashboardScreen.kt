package com.fairslice.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.fairslice.ui.components.GlassCard
import com.fairslice.ui.theme.AmberHighlight
import com.fairslice.ui.theme.IndigoPrimary
import com.fairslice.ui.theme.VioletSecondary

@Composable
fun DashboardScreen(
    onNavigateToNewSplit: () -> Unit,
    onNavigateToHistory: () -> Unit,
    onNavigateToSettings: () -> Unit,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(IndigoPrimary, VioletSecondary)
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "FairSlice",
                    style = MaterialTheme.typography.displayLarge,
                    color = Color.White
                )
                Row {
                    IconButton(onClick = onNavigateToHistory) {
                        Icon(Icons.Default.History, contentDescription = "History", tint = Color.White)
                    }
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings", tint = Color.White)
                    }
                }
            }

            GlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Your Balance",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        BalanceItem(label = "You're owed", amount = uiState.youAreOwed, color = AmberHighlight)
                        BalanceItem(label = "You owe", amount = uiState.youOwe, color = Color.White)
                    }
                }
            }

            Text(
                text = "Recent Splits",
                style = MaterialTheme.typography.titleLarge,
                color = Color.White,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(uiState.recentBills) { bill ->
                    RecentSplitItem(bill)
                }
            }
        }

        FloatingActionButton(
            onClick = onNavigateToNewSplit,
            containerColor = AmberHighlight,
            contentColor = IndigoPrimary,
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp)
                .size(64.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "New Split",
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

@Composable
fun BalanceItem(label: String, amount: Double, color: Color) {
    Column {
        Text(text = label, fontSize = 14.sp, color = Color.White.copy(alpha = 0.7f))
        Text(
            text = "$${String.format("%.2f", amount)}",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )
    }
}

@Composable
fun RecentSplitItem(bill: com.fairslice.data.models.Bill) {
    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = bill.name, fontWeight = FontWeight.SemiBold, color = Color.White)
                Text(text = bill.category, fontSize = 12.sp, color = Color.White.copy(alpha = 0.6f))
            }
            Text(
                text = "$${String.format("%.2f", bill.totalAmountCents.toDouble() / 100.0)}",
                fontWeight = FontWeight.Bold,
                color = AmberHighlight
            )
        }
    }
}
