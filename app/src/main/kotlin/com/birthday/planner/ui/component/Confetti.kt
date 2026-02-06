package com.birthday.planner.ui.component

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import com.birthday.planner.ui.theme.*
import kotlin.random.Random

@Composable
fun ConfettiCelebration(modifier: Modifier = Modifier) {
    val confettiColors = listOf(PartyPink, PartyYellow, PartyBlue, BrightTeal, ElectricPurple)
    val particles = remember {
        List(50) {
            ConfettiParticle(
                color = confettiColors.random(),
                initialOffset = Offset(Random.nextFloat(), Random.nextFloat()),
                velocity = Offset(Random.nextFloat() * 0.02f - 0.01f, Random.nextFloat() * 0.05f + 0.02f),
                size = Random.nextFloat() * 10f + 5f
            )
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "confetti")
    val progress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "progress"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        particles.forEach { particle ->
            val y = (particle.initialOffset.y + progress * 2f) % 1f
            val x = (particle.initialOffset.x + progress * particle.velocity.x * 10f) % 1f
            drawCircle(
                color = particle.color,
                radius = particle.size,
                center = Offset(x * size.width, y * size.height)
            )
        }
    }
}

data class ConfettiParticle(
    val color: Color,
    val initialOffset: Offset,
    val velocity: Offset,
    val size: Float
)
