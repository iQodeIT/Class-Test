package com.apartment.planner.ui.screens.room

import androidx.compose.foundation.clickable
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.apartment.planner.data.local.entity.PinEntity
import com.apartment.planner.ui.theme.LocalSpacing
import com.apartment.planner.ui.theme.Terracotta
import com.apartment.planner.ui.viewmodel.RoomUiState
import com.apartment.planner.ui.viewmodel.RoomViewModel

@Composable
fun MoodBoardTab(
    uiState: RoomUiState,
    viewModel: RoomViewModel,
    onCompareShortlisted: () -> Unit
) {
    val spacing = LocalSpacing.current

    val shortlistedPins = uiState.pins.filter { it.status == "SHORTLISTED" }

    Box(modifier = Modifier.fillMaxSize()) {
        if (uiState.pins.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                Text("No pins yet. Add your first idea!", style = MaterialTheme.typography.bodyLarge)
            }
        } else {
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(spacing.standard),
                horizontalArrangement = Arrangement.spacedBy(spacing.standard),
                verticalItemSpacing = spacing.standard
            ) {
                items(uiState.pins) { pin ->
                    PinCard(pin = pin) {
                        // TODO: Navigate to Pin Details
                    }
                }
            }
        }

        AnimatedVisibility(
            visible = shortlistedPins.size >= 2,
            modifier = Modifier
                .align(androidx.compose.ui.Alignment.BottomCenter)
                .padding(spacing.standard)
        ) {
            Button(
                onClick = onCompareShortlisted,
                colors = ButtonDefaults.buttonColors(containerColor = Terracotta),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
            ) {
                Text("Compare shortlisted (${shortlistedPins.size})")
            }
        }
    }
}

@Composable
fun PinCard(pin: PinEntity, onClick: () -> Unit) {
    val spacing = LocalSpacing.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column {
            AsyncImage(
                model = pin.imageUri,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 100.dp, max = 250.dp)
                    .clip(MaterialTheme.shapes.medium),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.padding(spacing.compact)) {
                Text(
                    text = pin.category,
                    style = MaterialTheme.typography.labelSmall,
                    color = Terracotta
                )
                Text(
                    text = pin.notes,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 2,
                    fontWeight = FontWeight.Medium
                )
                if (pin.status == "SHORTLISTED") {
                    Spacer(modifier = Modifier.height(spacing.tight))
                    SuggestionChip(
                        onClick = {},
                        label = { Text("⭐ Shortlisted", fontSize = 10.sp) },
                        colors = SuggestionChipDefaults.suggestionChipColors(
                            containerColor = Color(0xFFFFD700).copy(alpha = 0.1f),
                            labelColor = Color(0xFFB8860B)
                        ),
                        border = null
                    )
                }
            }
        }
    }
}
