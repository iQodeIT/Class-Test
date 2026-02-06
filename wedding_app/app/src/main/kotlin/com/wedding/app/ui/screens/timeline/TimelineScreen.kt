package com.wedding.app.ui.screens.timeline

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.wedding.app.ui.viewmodel.WeddingViewModel

data class Milestone(val title: String, val date: String, val isCompleted: Boolean)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimelineScreen(viewModel: WeddingViewModel, onBack: () -> Unit) {
    val currentWedding by viewModel.currentWedding.collectAsState()
    val checklistItems by if (currentWedding != null) {
        viewModel.getChecklist(currentWedding!!.id).collectAsState(initial = emptyList())
    } else {
        remember { mutableStateOf(emptyList()) }
    }

    val milestones = remember(checklistItems) {
        checklistItems.map { Milestone(it.title, it.phase, it.isCompleted) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Wedding Timeline") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp)
        ) {
            itemsIndexed(milestones) { index, milestone ->
                TimelineItem(milestone, isLast = index == milestones.size - 1)
            }
        }
    }
}

@Composable
fun TimelineItem(milestone: Milestone, isLast: Boolean) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(
                        if (milestone.isCompleted) MaterialTheme.colorScheme.primary else Color.LightGray,
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (milestone.isCompleted) {
                    Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                }
            }
            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(80.dp)
                        .background(Color.LightGray)
                )
            }
        }

        Spacer(modifier = Modifier.width(24.dp))

        Column(modifier = Modifier.padding(bottom = 32.dp)) {
            Text(
                text = milestone.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = if (milestone.isCompleted) MaterialTheme.colorScheme.onBackground else Color.Gray
            )
            Text(
                text = milestone.date,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
    }
}
