package com.fairslice.ui.newsplit

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fairslice.ui.components.Numpad
import com.fairslice.ui.theme.AmberHighlight
import com.fairslice.ui.theme.BackgroundDeep
import com.fairslice.ui.theme.IndigoPrimary

@Composable
fun NewSplitScreen(
    onNext: (String, Float, String) -> Unit,
    viewModel: NewSplitViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    val categories = listOf("🍽️ Dining", "✈️ Travel", "🏠 Rent", "🎉 Party", "🛒 Groceries", "🎬 Ent.", "✏️ Custom")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDeep)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "New Split",
            style = MaterialTheme.typography.headlineLarge,
            color = Color.White,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        TextField(
            value = uiState.billName,
            onValueChange = { viewModel.onBillNameChange(it) },
            placeholder = { Text("What's this for? (e.g. Cabo Condo)", color = Color.Gray) },
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                unfocusedIndicatorColor = Color.White.copy(alpha = 0.5f),
                focusedIndicatorColor = AmberHighlight,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth(),
            textStyle = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = if (uiState.totalAmount.isEmpty()) "$0.00" else "$${uiState.totalAmount}",
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold,
            color = AmberHighlight,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(categories) { category ->
                FilterChip(
                    selected = uiState.category == category,
                    onClick = { viewModel.onCategoryChange(category) },
                    label = { Text(category) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = AmberHighlight,
                        selectedLabelColor = IndigoPrimary,
                        labelColor = Color.White
                    )
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Numpad(
            onNumberClick = { num -> viewModel.onAmountChange(uiState.totalAmount + num) },
            onDeleteClick = {
                if (uiState.totalAmount.isNotEmpty()) {
                    viewModel.onAmountChange(uiState.totalAmount.dropLast(1))
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                onNext(
                    uiState.billName.ifEmpty { "Shared Bill" },
                    uiState.totalAmount.toFloatOrNull() ?: 0f,
                    uiState.category
                )
            },
            enabled = uiState.totalAmount.isNotEmpty(),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = AmberHighlight),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("Next: Add People", color = IndigoPrimary, fontWeight = FontWeight.Bold)
        }
    }
}
