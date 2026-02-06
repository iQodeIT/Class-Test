package com.wedding.app.ui.screens.onboarding

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.wedding.app.ui.viewmodel.WeddingViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(viewModel: WeddingViewModel, onComplete: () -> Unit) {
    val pagerState = rememberPagerState(pageCount = { 3 })
    var name by remember { mutableStateOf("") }
    var partnerName by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize()) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            OnboardingPage(page)
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(24.dp)
                .background(Color.White.copy(alpha = 0.8f), MaterialTheme.shapes.large)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (pagerState.currentPage == 2) {
                Text(
                    text = "Let's Get Started",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Your Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = partnerName,
                    onValueChange = { partnerName = it },
                    label = { Text("Partner's Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = {
                        viewModel.createUser(name, partnerName)
                        onComplete()
                    },
                    enabled = name.isNotBlank(),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Begin Planning")
                }
            } else {
                Text(
                    text = when(pagerState.currentPage) {
                        0 -> "Plan Your Dream Wedding"
                        1 -> "Visualize Your Style"
                        else -> ""
                    },
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = when(pagerState.currentPage) {
                        0 -> "Organize every detail from venue to cake in one beautiful place."
                        1 -> "Create stunning mood boards and make decisions effortlessly."
                        else -> ""
                    },
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(32.dp))
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    repeat(3) { iteration ->
                        val color = if (pagerState.currentPage == iteration) MaterialTheme.colorScheme.primary else Color.LightGray
                        Box(
                            modifier = Modifier
                                .padding(4.dp)
                                .clip(androidx.compose.foundation.shape.CircleShape)
                                .background(color)
                                .size(8.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun OnboardingPage(page: Int) {
    val gradient = when(page) {
        0 -> Brush.verticalGradient(listOf(Color(0xFFF4E4E6), Color(0xFFFAF7F5)))
        1 -> Brush.verticalGradient(listOf(Color(0xFFFAF7F5), Color(0xFFA8BACC)))
        else -> Brush.verticalGradient(listOf(Color(0xFFA8BACC), Color(0xFFFFFCF9)))
    }

    Box(modifier = Modifier.fillMaxSize().background(gradient)) {
        // Here we could add parallax images
    }
}
