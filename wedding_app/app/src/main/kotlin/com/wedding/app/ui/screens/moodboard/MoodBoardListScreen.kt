package com.wedding.app.ui.screens.moodboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.wedding.app.data.local.entity.BoardEntity
import com.wedding.app.ui.viewmodel.WeddingViewModel

@Composable
fun MoodBoardListScreen(viewModel: WeddingViewModel, onBoardClick: (Long) -> Unit) {
    val currentWedding by viewModel.currentWedding.collectAsState()

    currentWedding?.let { wedding ->
        val boards by viewModel.getBoards(wedding.id).collectAsState(initial = emptyList())

        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Text(
                text = "Mood Boards",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(16.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(boards) { board ->
                    MoodBoardCard(board, onBoardClick)
                }
            }
        }
    }
}

@Composable
fun MoodBoardCard(board: BoardEntity, onBoardClick: (Long) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .clickable { onBoardClick(board.id) },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = board.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
