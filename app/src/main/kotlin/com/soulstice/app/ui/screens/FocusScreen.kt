package com.soulstice.app.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.soulstice.app.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun FocusScreen() {
    var timeLeft by remember { mutableLongStateOf(25 * 60 * 1000L) }
    var isRunning by remember { mutableStateOf(false) }
    val totalTime = 25 * 60 * 1000L

    LaunchedEffect(isRunning) {
        if (isRunning) {
            while (timeLeft > 0) {
                delay(1000L)
                timeLeft -= 1000L
            }
            isRunning = false
        }
    }

    val minutes = (timeLeft / 1000) / 60
    val seconds = (timeLeft / 1000) % 60

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Focus Session",
            style = MaterialTheme.typography.headlineMedium,
            color = Taupe900
        )
        Text(
            text = "Deep work in progress",
            style = MaterialTheme.typography.bodyMedium,
            color = Taupe500
        )

        Spacer(modifier = Modifier.height(64.dp))

        Box(contentAlignment = Alignment.Center) {
            val progress = timeLeft.toFloat() / totalTime

            Canvas(modifier = Modifier.size(240.dp)) {
                drawCircle(
                    color = Taupe100,
                    style = Stroke(width = 12.dp.toPx())
                )
                drawArc(
                    color = Sage500,
                    startAngle = -90f,
                    sweepAngle = 360f * progress,
                    useCenter = false,
                    style = Stroke(width = 12.dp.toPx(), cap = StrokeCap.Round)
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = String.format("%02d:%02d", minutes, seconds),
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    color = Taupe900
                )
                Text(
                    text = "minutes left",
                    fontSize = 14.sp,
                    color = Taupe500
                )
            }
        }

        Spacer(modifier = Modifier.height(64.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    isRunning = false
                    timeLeft = 25 * 60 * 1000L
                },
                modifier = Modifier.size(48.dp)
            ) {
                Icon(Icons.Default.Refresh, contentDescription = "Reset", tint = Taupe500)
            }

            LargeFloatingActionButton(
                onClick = { isRunning = !isRunning },
                containerColor = Sage600,
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Icon(
                    imageVector = if (isRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = if (isRunning) "Pause" else "Start",
                    modifier = Modifier.size(36.dp)
                )
            }

            IconButton(
                onClick = { /* Settings */ },
                modifier = Modifier.size(48.dp)
            ) {
                // Placeholder for settings or skip
                Text("SKIP", color = Taupe500, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
        }

        Spacer(modifier = Modifier.height(48.dp))

        Text(
            text = "Today: 4 sessions completed",
            style = MaterialTheme.typography.bodySmall,
            color = Taupe500
        )
    }
}
