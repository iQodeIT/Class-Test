package com.fairslice.ui.results

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import android.content.Intent
import android.net.Uri
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.fairslice.ui.components.GlassCard
import com.fairslice.ui.theme.*
import nl.dionsegijn.konfetti.compose.KonfettiView
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import java.util.concurrent.TimeUnit

@Composable
fun ResultsScreen(
    onDone: () -> Unit,
    viewModel: ResultsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    var showConfetti by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.isLoading) {
        if (!uiState.isLoading) {
            showConfetti = true
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDeep)
    ) {
        if (showConfetti) {
            KonfettiView(
                modifier = Modifier.fillMaxSize(),
                parties = listOf(
                    Party(
                        speed = 0f,
                        maxSpeed = 30f,
                        damping = 0.9f,
                        spread = 360,
                        colors = listOf(0xFF4B0082.toInt(), 0xFF8A2BE2.toInt(), 0xFFFFBF00.toInt()),
                        position = Position.Relative(0.5, 0.3),
                        emitter = Emitter(duration = 100, TimeUnit.MILLISECONDS).max(100)
                    )
                )
            )
        }

        uiState.billWithParticipants?.let { data ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
            ) {
                Text(
                    text = "The Fair Split",
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color.White
                )
                val totalAmount = data.bill.totalAmountCents.toDouble() / 100.0
                Text(
                    text = "${data.bill.name} • $${String.format("%.2f", totalAmount)}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = AmberHighlight,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(data.participants) { participant ->
                        ResultCard(participant, data.bill.totalAmountCents)
                    }

                    item {
                        CalculationExplanation()
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            val summary = data.participants.joinToString("\n") {
                                "${it.name}: $${String.format("%.2f", it.amountOwedCents.toDouble() / 100.0)}"
                            }
                            val intent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_TEXT, "FairSlice Split for ${data.bill.name}:\n$summary")
                            }
                            context.startActivity(Intent.createChooser(intent, "Share Split"))
                        },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                        border = ButtonDefaults.outlinedButtonBorder.copy(brush = androidx.compose.ui.graphics.SolidColor(Color.White.copy(alpha = 0.5f)))
                    ) {
                        Icon(Icons.Default.Share, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Share")
                    }
                    Button(
                        onClick = onDone,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(containerColor = AmberHighlight),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null, tint = IndigoPrimary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Done", color = IndigoPrimary, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        if (uiState.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = AmberHighlight)
        }
    }
}

@Composable
fun ResultCard(participant: com.fairslice.data.models.Participant, totalBillCents: Long) {
    val amountOwed = participant.amountOwedCents.toDouble() / 100.0
    val totalBill = totalBillCents.toDouble() / 100.0
    val percentage = if (totalBill > 0) (amountOwed / totalBill * 100).toInt() else 0

    GlassCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(IndigoPrimary),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = participant.name.take(1).uppercase(),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(text = participant.name, fontWeight = FontWeight.SemiBold, color = Color.White)
                    Text(text = "$percentage% of bill", fontSize = 12.sp, color = Color.White.copy(alpha = 0.6f))
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "$${String.format("%.2f", amountOwed)}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = AmberHighlight
                )

                Row {
                    val context = LocalContext.current
                    TextButton(
                        onClick = {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("venmo://paycharge?txn=pay&amount=$amountOwed&note=FairSlice for Split"))
                            try { context.startActivity(intent) } catch (e: Exception) { /* Venmo not installed */ }
                        },
                        contentPadding = PaddingValues(0.dp),
                        modifier = Modifier.height(24.dp)
                    ) {
                        Text("Venmo", fontSize = 10.sp, color = Color.Cyan)
                    }
                }
                Surface(
                    shape = CircleShape,
                    color = SuccessGreen.copy(alpha = 0.2f),
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Text(
                        text = "💛 Fair Share",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        fontSize = 10.sp,
                        color = SuccessGreen,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun CalculationExplanation() {
    var expanded by remember { mutableStateOf(false) }

    GlassCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Info, contentDescription = null, tint = Color.White.copy(alpha = 0.7f))
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("How was this calculated?", color = Color.White.copy(alpha = 0.7f), fontSize = 14.sp)
                }
                TextButton(onClick = { expanded = !expanded }) {
                    Text(if (expanded) "Less" else "More", color = AmberHighlight)
                }
            }

            AnimatedVisibility(visible = expanded) {
                Text(
                    text = "We normalized everyone's income to an annual figure and split the bill proportionally. Alice's share = (Alice's Income / Total Group Income) × Total Bill. This ensures everyone pays an equal percentage of their earnings.",
                    color = Color.White.copy(alpha = 0.6f),
                    fontSize = 13.sp,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}
