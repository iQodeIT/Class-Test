package com.wedding.app.ui.screens.dashboard

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.wedding.app.data.local.entity.WeddingEntity
import com.wedding.app.ui.viewmodel.WeddingViewModel
import java.util.concurrent.TimeUnit

@Composable
fun DashboardScreen(
    viewModel: WeddingViewModel,
    onMoodBoardClick: () -> Unit,
    onChecklistClick: () -> Unit,
    onBudgetClick: () -> Unit,
    onTimelineClick: () -> Unit
) {
    val weddings by viewModel.weddings.collectAsState()
    val currentWedding by viewModel.currentWedding.collectAsState()

    // If no current wedding but we have weddings, select the first one
    LaunchedEffect(weddings) {
        if (currentWedding == null && weddings.isNotEmpty()) {
            viewModel.selectWedding(weddings.first())
        }
    }

    currentWedding?.let { wedding ->
        var visible by remember { mutableStateOf(false) }
        LaunchedEffect(Unit) { visible = true }

        Scaffold(
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { /* Quick Add */ },
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Quick Add")
                }
            }
        ) { padding ->
            AnimatedVisibility(
                visible = visible,
                enter = fadeIn(animationSpec = spring(stiffness = Spring.StiffnessLow)) +
                        slideInVertically(initialOffsetY = { it / 2 })
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    HeroSection(wedding)
                    DashboardGrid(onMoodBoardClick, onChecklistClick, onBudgetClick, onTimelineClick)
                }
            }
        }
    } ?: Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
fun HeroSection(wedding: WeddingEntity) {
    val daysLeft = remember(wedding.date) {
        val diff = wedding.date - System.currentTimeMillis()
        TimeUnit.MILLISECONDS.toDays(diff).coerceAtLeast(0)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(260.dp)
            .padding(16.dp),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                            MaterialTheme.colorScheme.secondary.copy(alpha = 0.5f)
                        )
                    )
                )
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = wedding.name,
                    style = MaterialTheme.typography.displayLarge,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "$daysLeft Days to Go",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = wedding.location ?: "Set Location",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                )
            }
        }
    }
}

@Composable
fun DashboardGrid(
    onMoodBoardClick: () -> Unit,
    onChecklistClick: () -> Unit,
    onBudgetClick: () -> Unit,
    onTimelineClick: () -> Unit
) {
    val items = listOf(
        DashboardItem("Mood Boards", Icons.Default.Brush, "8 Boards", onMoodBoardClick),
        DashboardItem("Checklist", Icons.Default.List, "12 of 48 Tasks", onChecklistClick),
        DashboardItem("Budget", Icons.Default.Payments, "75% Spent", onBudgetClick),
        DashboardItem("Timeline", Icons.Default.Assignment, "Next: Cake Tasting", onTimelineClick)
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(items) { item ->
            DashboardCard(item)
        }
    }
}

data class DashboardItem(
    val title: String,
    val icon: ImageVector,
    val subtitle: String,
    val onClick: () -> Unit
)

@Composable
fun DashboardCard(item: DashboardItem) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .clickable { item.onClick() },
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.6f),
        tonalElevation = 8.dp,
        border = androidx.compose.foundation.BorderStroke(0.5.dp, Color.White.copy(alpha = 0.3f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(32.dp)
            )
            Column {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = item.subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
    }
}
