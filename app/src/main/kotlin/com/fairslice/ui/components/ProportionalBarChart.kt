package com.fairslice.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

data class BarData(
    val value: Float,
    val color: Color
)

@Composable
fun ProportionalBarChart(
    bars: List<BarData>,
    modifier: Modifier = Modifier
) {
    val total = bars.sumOf { it.value.toDouble() }.toFloat().coerceAtLeast(1f)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(12.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(Color.White.copy(alpha = 0.1f)),
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        bars.forEach { bar ->
            val weight = (bar.value / total).coerceAtLeast(0.01f)
            val animatedWeight by animateFloatAsState(
                targetValue = weight,
                animationSpec = spring(),
                label = "barWeight"
            )

            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(animatedWeight)
                    .background(bar.color)
            )
        }
    }
}
