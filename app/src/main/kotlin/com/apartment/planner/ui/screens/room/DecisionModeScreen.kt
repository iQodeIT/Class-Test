package com.apartment.planner.ui.screens.room

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.apartment.planner.data.local.entity.PinEntity
import com.apartment.planner.ui.theme.LocalSpacing
import com.apartment.planner.ui.theme.Terracotta

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DecisionModeScreen(
    shortlistedPins: List<PinEntity>,
    onBack: () -> Unit,
    onConfirm: (PinEntity) -> Unit
) {
    val spacing = LocalSpacing.current
    var selectedPinId by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Compare & Decide") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = {
            if (selectedPinId != null) {
                Button(
                    onClick = {
                        val selected = shortlistedPins.find { it.id == selectedPinId }
                        if (selected != null) onConfirm(selected)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(spacing.standard),
                    colors = ButtonDefaults.buttonColors(containerColor = Terracotta)
                ) {
                    Icon(Icons.Default.Check, contentDescription = null)
                    Spacer(modifier = Modifier.width(spacing.compact))
                    Text("Confirm Choice")
                }
            }
        }
    ) { paddingValues ->
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(spacing.compact)
                .horizontalScroll(rememberScrollState())
        ) {
            shortlistedPins.forEach { pin ->
                ComparisonCard(
                    pin = pin,
                    isSelected = selectedPinId == pin.id,
                    onSelect = { selectedPinId = pin.id }
                )
            }
        }
    }
}

@Composable
fun ComparisonCard(pin: PinEntity, isSelected: Boolean, onSelect: () -> Unit) {
    val spacing = LocalSpacing.current
    Card(
        modifier = Modifier
            .width(280.dp)
            .fillMaxHeight()
            .padding(spacing.compact)
            .clickable(onClick = onSelect),
        border = if (isSelected) BorderStroke(2.dp, Terracotta) else null,
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)
        )
    ) {
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            AsyncImage(
                model = pin.imageUri,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(MaterialTheme.shapes.medium),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.padding(spacing.standard)) {
                Text(text = pin.category, style = MaterialTheme.typography.labelMedium, color = Terracotta)
                Text(text = pin.notes, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(spacing.standard))
                Text(text = "Size: ${pin.sizeTag}", style = MaterialTheme.typography.bodyMedium)
                if (pin.estimatedPrice != null) {
                    Text(text = "Price: $${pin.estimatedPrice}", style = MaterialTheme.typography.bodyMedium)
                }

                Spacer(modifier = Modifier.height(spacing.comfortable))

                RadioButton(
                    selected = isSelected,
                    onClick = onSelect,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    colors = RadioButtonDefaults.colors(selectedColor = Terracotta)
                )
            }
        }
    }
}
