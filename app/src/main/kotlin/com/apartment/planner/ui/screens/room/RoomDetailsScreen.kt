package com.apartment.planner.ui.screens.room

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.apartment.planner.ui.theme.LocalSpacing
import com.apartment.planner.ui.theme.Terracotta
import com.apartment.planner.ui.viewmodel.RoomViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoomDetailsScreen(
    navController: NavController,
    viewModel: RoomViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val spacing = LocalSpacing.current
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Mood Board", "Checklist", "Budget")

    var isDecisionMode by remember { mutableStateOf(false) }
    var showAddPinDialog by remember { mutableStateOf(false) }

    if (isDecisionMode) {
        DecisionModeScreen(
            shortlistedPins = uiState.pins.filter { it.status == "SHORTLISTED" },
            onBack = { isDecisionMode = false },
            onConfirm = { pin ->
                viewModel.confirmPinChoice(pin)
                isDecisionMode = false
            }
        )
        return
    }

    if (showAddPinDialog) {
        AddPinDialog(
            onDismiss = { showAddPinDialog = false },
            onAdd = { uri, notes ->
                viewModel.addPin(uri, notes)
                showAddPinDialog = false
            }
        )
    }

    Scaffold(
        floatingActionButton = {
            if (selectedTab == 0) {
                FloatingActionButton(
                    onClick = { showAddPinDialog = true },
                    containerColor = Terracotta,
                    contentColor = Color.White
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Pin")
                }
            }
        },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Column(horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally) {
                        Text(
                            text = "${uiState.room?.icon ?: ""} ${uiState.room?.name ?: ""}".uppercase(),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { /* TODO: More */ }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "More")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Progress Bar
            LinearProgressIndicator(
                progress = 0.75f,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp),
                color = Terracotta,
                trackColor = Terracotta.copy(alpha = 0.1f)
            )

            Text(
                text = "12 of 16 items complete",
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(start = spacing.standard, top = 4.dp),
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
            )

            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = MaterialTheme.colorScheme.background,
                contentColor = Terracotta,
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = Terracotta,
                        height = 3.dp
                    )
                },
                divider = {}
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                text = title,
                                style = MaterialTheme.typography.titleSmall,
                                color = if (selectedTab == index) Terracotta else Color.Gray
                            )
                        }
                    )
                }
            }

            Box(modifier = Modifier.weight(1f)) {
                when (selectedTab) {
                    0 -> MoodBoardTab(
                        uiState = uiState,
                        viewModel = viewModel,
                        onCompareShortlisted = { isDecisionMode = true }
                    )
                    1 -> ChecklistTab(uiState, viewModel)
                    2 -> BudgetTab(uiState, viewModel)
                }
            }
        }
    }
}

@Composable
fun AddPinDialog(onDismiss: () -> Unit, onAdd: (String, String) -> Unit) {
    var uri by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    // Basic "Smart" extraction
    LaunchedEffect(uri) {
        if (uri.contains("pinterest.com") && notes.isEmpty()) {
            notes = "Idea from Pinterest"
        } else if (uri.contains("ikea.com") && notes.isEmpty()) {
            notes = "Furniture from IKEA"
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add New Pin") },
        text = {
            Column {
                OutlinedTextField(
                    value = uri,
                    onValueChange = { uri = it },
                    label = { Text("Image URL") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Notes") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(onClick = { onAdd(uri, notes) }) {
                Text("Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
