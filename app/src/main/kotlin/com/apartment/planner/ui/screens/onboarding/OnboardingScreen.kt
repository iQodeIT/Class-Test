package com.apartment.planner.ui.screens.onboarding

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.apartment.planner.ui.navigation.Screen
import com.apartment.planner.ui.theme.LocalSpacing
import com.apartment.planner.ui.theme.Terracotta
import com.apartment.planner.ui.theme.WarmBeige
import kotlinx.coroutines.launch

data class OnboardingPage(
    val title: String,
    val description: String,
    val color: Color
)

val onboardingPages = listOf(
    OnboardingPage(
        "Your space, intentionally designed",
        "Create a home that reflects your values and brings you peace.",
        WarmBeige
    ),
    OnboardingPage(
        "From inspiration to reality",
        "Organize your ideas and turn them into a beautiful living space.",
        WarmBeige
    ),
    OnboardingPage(
        "Decide with confidence",
        "Compare options and make the right choices for your small space.",
        WarmBeige
    )
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(navController: NavController) {
    val pagerState = rememberPagerState(pageCount = { onboardingPages.size })
    val scope = rememberCoroutineScope()
    val spacing = LocalSpacing.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(WarmBeige, Color.White)
                )
            )
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { pageIndex ->
            val page = onboardingPages[pageIndex]
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(spacing.spacious),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Placeholder for image
                Box(
                    modifier = Modifier
                        .size(280.dp)
                        .clip(MaterialTheme.shapes.large)
                        .background(page.color.copy(alpha = 0.3f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Image Placeholder",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Spacer(modifier = Modifier.height(spacing.spacious))

                Text(
                    text = page.title,
                    style = MaterialTheme.typography.displayLarge.copy(
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(spacing.standard))

                Text(
                    text = page.description,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                )
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(spacing.spacious),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Page Indicator
            Row {
                repeat(onboardingPages.size) { iteration ->
                    val color = if (pagerState.currentPage == iteration) Terracotta else Color.LightGray
                    Box(
                        modifier = Modifier
                            .padding(2.dp)
                            .clip(CircleShape)
                            .background(color)
                            .size(8.dp)
                    )
                }
            }

            Button(
                onClick = {
                    if (pagerState.currentPage < onboardingPages.size - 1) {
                        scope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    } else {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Onboarding.route) { inclusive = true }
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Terracotta)
            ) {
                Text(if (pagerState.currentPage == onboardingPages.size - 1) "Get Started" else "Next")
            }
        }
    }
}
