package com.birthday.planner.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.birthday.planner.ui.component.BoardCard
import com.birthday.planner.ui.component.PartyCard
import com.birthday.planner.ui.viewmodel.PartyViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PartyWorkspaceScreen(
    viewModel: PartyViewModel,
    onBackClick: () -> Unit,
    onBoardClick: (Long) -> Unit,
    onChecklistClick: () -> Unit,
    onBudgetClick: () -> Unit
) {
    val party by viewModel.selectedParty.collectAsState()
    val boards by viewModel.boards.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(party?.partyTitle ?: "Party Workspace") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            party?.let {
                PartyCard(
                    party = it,
                    onClick = { /* maybe edit */ },
                    modifier = Modifier.padding(16.dp)
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onChecklistClick,
                    modifier = Modifier.weight(1f),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
                ) {
                    Text("Tasks")
                }
                Button(
                    onClick = onBudgetClick,
                    modifier = Modifier.weight(1f),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
                ) {
                    Text("Budget")
                }
            }

            Text(
                text = "Theme Boards",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(16.dp)
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(boards) { (board, count) ->
                    BoardCard(
                        board = board,
                        pinCount = count,
                        onClick = { onBoardClick(board.id) }
                    )
                }
            }
        }
    }
}
