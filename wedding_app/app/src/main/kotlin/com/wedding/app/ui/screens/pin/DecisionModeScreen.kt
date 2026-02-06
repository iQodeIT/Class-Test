package com.wedding.app.ui.screens.pin

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.airbnb.lottie.compose.*
import com.wedding.app.data.local.entity.PinEntity
import com.wedding.app.ui.viewmodel.WeddingViewModel

@Composable
fun DecisionModeScreen(viewModel: WeddingViewModel, boardId: Long, onDecisionMade: () -> Unit) {
    val pins by viewModel.getPins(boardId).collectAsState(initial = emptyList())
    val shortlistedPins = remember(pins) { pins.filter { it.status == "SHORTLISTED" || it.status == "INSPIRED" }.take(3) }
    var selectedPinId by remember { mutableStateOf<Long?>(null) }
    var showCelebration by remember { mutableStateOf(false) }

    if (showCelebration) {
        AlertDialog(
            onDismissRequest = { onDecisionMade() },
            title = { Text("It's a Match! 💖") },
            text = {
                Box(modifier = Modifier.size(200.dp)) {
                    CelebrationAnimation()
                }
            },
            confirmButton = {
                Button(onClick = { onDecisionMade() }) { Text("Perfect!") }
            }
        )
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = "Make a Choice",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth().weight(1f),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            shortlistedPins.forEach { pin ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(16.dp))
                        .border(
                            width = if (selectedPinId == pin.id) 4.dp else 0.dp,
                            color = if (selectedPinId == pin.id) MaterialTheme.colorScheme.secondary else Color.Transparent,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .clickable { selectedPinId = pin.id }
                ) {
                    AsyncImage(
                        model = pin.imageUrl,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                selectedPinId?.let { viewModel.markPinAsFinal(it, boardId) }
                showCelebration = true
            },
            enabled = selectedPinId != null,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Lock Choice 👑")
        }
    }
}

@Composable
fun CelebrationAnimation() {
    val composition by rememberLottieComposition(LottieCompositionSpec.Url("https://assets9.lottiefiles.com/packages/lf20_u4yrau.json"))
    LottieAnimation(composition, iterations = LottieConstants.IterateForever)
}
