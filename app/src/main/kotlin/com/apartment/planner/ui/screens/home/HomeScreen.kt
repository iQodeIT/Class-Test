package com.apartment.planner.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.apartment.planner.data.local.entity.RoomEntity
import com.apartment.planner.ui.navigation.Screen
import com.apartment.planner.ui.theme.LocalSpacing
import com.apartment.planner.ui.theme.Terracotta
import com.apartment.planner.ui.theme.WarmBeige
import com.apartment.planner.ui.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val spacing = LocalSpacing.current

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* TODO: Add Room */ },
                containerColor = Terracotta,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Room")
            }
        },
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    selected = true,
                    onClick = { },
                    icon = { Text("🏠", fontSize = 24.sp) },
                    label = { Text("Home") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate(Screen.Budget.route) },
                    icon = { Text("💰", fontSize = 24.sp) },
                    label = { Text("Budget") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate(Screen.Settings.route) },
                    icon = { Text("⚙️", fontSize = 24.sp) },
                    label = { Text("Settings") }
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Hero Card
            uiState.apartment?.let { apartment ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(spacing.standard)
                        .height(200.dp),
                    shape = MaterialTheme.shapes.medium,
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(Terracotta, WarmBeige)
                                )
                            )
                            .padding(spacing.standard)
                    ) {
                        Column(modifier = Modifier.align(Alignment.BottomStart)) {
                            Text(
                                text = apartment.name,
                                style = MaterialTheme.typography.headlineMedium,
                                color = Color.White
                            )
                            Text(
                                text = "${apartment.type} • ${apartment.sizeRange?.replace("_", " ")}",
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color.White.copy(alpha = 0.8f)
                            )
                            Spacer(modifier = Modifier.height(spacing.standard))
                            LinearProgressIndicator(
                                progress = 0.68f,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                                    .clip(CircleShape),
                                color = Color.White,
                                trackColor = Color.White.copy(alpha = 0.2f)
                            )
                            Text(
                                text = "Overall Progress: 68%",
                                style = MaterialTheme.typography.labelMedium,
                                color = Color.White,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                }
            }

            Text(
                text = "Rooms",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(horizontal = spacing.standard, vertical = spacing.compact)
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(spacing.standard),
                horizontalArrangement = Arrangement.spacedBy(spacing.standard),
                verticalArrangement = Arrangement.spacedBy(spacing.standard),
                modifier = Modifier.weight(1f)
            ) {
                items(uiState.rooms) { room ->
                    RoomCard(room = room) {
                        navController.navigate(Screen.RoomDetails.createRoute(room.id))
                    }
                }
            }
        }
    }
}

@Composable
fun RoomCard(room: RoomEntity, onClick: () -> Unit) {
    val spacing = LocalSpacing.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .clickable(onClick = onClick),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(spacing.standard),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = room.icon, fontSize = 48.sp)
            Spacer(modifier = Modifier.height(spacing.compact))
            Text(
                text = room.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(spacing.tight))
            LinearProgressIndicator(
                progress = 0.45f,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .clip(CircleShape),
                color = Terracotta,
                trackColor = Terracotta.copy(alpha = 0.1f)
            )
        }
    }
}
