package com.birthday.planner.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.birthday.planner.ui.component.PinCard
import com.birthday.planner.ui.viewmodel.BoardViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoardDetailScreen(
    viewModel: BoardViewModel,
    onBackClick: () -> Unit,
    onAddPinClick: () -> Unit,
    onPinClick: (Long) -> Unit,
    onDecisionModeClick: () -> Unit
) {
    val pins by viewModel.pins.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ideas") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = onDecisionModeClick) {
                        Icon(Icons.Default.Star, contentDescription = "Decision Mode")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddPinClick) {
                Icon(Icons.Default.Add, contentDescription = "Add Idea")
            }
        }
    ) { padding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            items(pins) { pin ->
                PinCard(
                    pin = pin,
                    onClick = { onPinClick(pin.id) }
                )
            }
        }
    }
}
