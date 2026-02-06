package com.birthday.planner.ui.screen

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.birthday.planner.data.model.PartyType
import com.birthday.planner.data.model.ThemeSuggestion
import com.birthday.planner.ui.component.PrimaryGradientButton
import com.birthday.planner.ui.theme.Coral
import com.birthday.planner.ui.theme.SunsetOrange
import com.birthday.planner.ui.viewmodel.ThemeGeneratorViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThemeGeneratorScreen(
    viewModel: ThemeGeneratorViewModel,
    onBackClick: () -> Unit,
    onUseTheme: (ThemeSuggestion) -> Unit
) {
    val step by viewModel.step.collectAsState()
    val age by viewModel.age.collectAsState()
    val partyType by viewModel.partyType.collectAsState()
    val suggestions by viewModel.suggestions.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Theme Generator ✨") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            AnimatedContent(
                targetState = step,
                transitionSpec = {
                    if (targetState > initialState) {
                        slideInHorizontally { it } + fadeIn() togetherWith
                                slideOutHorizontally { -it } + fadeOut()
                    } else {
                        slideInHorizontally { -it } + fadeIn() togetherWith
                                slideOutHorizontally { it } + fadeOut()
                    }
                },
                label = "stepTransition"
            ) { targetStep ->
                Column(
                    modifier = Modifier.fillMaxSize().padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    when (targetStep) {
                        1 -> StepAge(age, onAgeChange = { viewModel.setAge(it) }, onNext = { viewModel.nextStep() })
                        2 -> StepInterests(onNext = { viewModel.nextStep() })
                        3 -> StepVibe(partyType, onNext = { viewModel.nextStep() })
                        4 -> ResultsScreen(suggestions, onUseTheme)
                    }
                }
            }
        }
    }
}

@Composable
fun StepAge(age: Int, onAgeChange: (Int) -> Unit, onNext: () -> Unit) {
    Text("How old is the birthday person?", style = MaterialTheme.typography.headlineSmall, textAlign = TextAlign.Center)
    Spacer(modifier = Modifier.height(32.dp))
    Text(text = age.toString(), fontSize = 64.sp, fontWeight = FontWeight.Bold, color = Coral)
    Slider(
        value = age.toFloat(),
        onValueChange = { onAgeChange(it.toInt()) },
        valueRange = 1f..100f,
        modifier = Modifier.padding(horizontal = 32.dp)
    )
    Spacer(modifier = Modifier.height(48.dp))
    PrimaryGradientButton(text = "Next →", onClick = onNext)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StepInterests(onNext: () -> Unit) {
    Text("What are their interests?", style = MaterialTheme.typography.headlineSmall, textAlign = TextAlign.Center)
    Spacer(modifier = Modifier.height(24.dp))
    // Mock interests
    val interests = listOf("🦸 Superheroes", "🦄 Unicorns", "⚽ Sports", "🎮 Gaming", "🎨 Arts", "🚀 Space")
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        interests.chunked(2).forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                row.forEach { interest ->
                    FilterChip(
                        selected = false,
                        onClick = {},
                        label = { Text(interest) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
    Spacer(modifier = Modifier.height(48.dp))
    PrimaryGradientButton(text = "Next →", onClick = onNext)
}

@Composable
fun StepVibe(partyType: PartyType, onNext: () -> Unit) {
    Text("What's the party vibe?", style = MaterialTheme.typography.headlineSmall, textAlign = TextAlign.Center)
    Spacer(modifier = Modifier.height(32.dp))
    Text("Type: ${partyType.name}", fontWeight = FontWeight.Bold, color = Coral)
    Spacer(modifier = Modifier.height(16.dp))
    Text("Select Vibe Intensity")
    Slider(value = 0.5f, onValueChange = {})
    Spacer(modifier = Modifier.height(48.dp))
    PrimaryGradientButton(text = "Generate Themes! ✨", onClick = onNext)
}

@Composable
fun ResultsScreen(suggestions: List<ThemeSuggestion>, onUseTheme: (ThemeSuggestion) -> Unit) {
    Text("Your Theme Ideas ✨", style = MaterialTheme.typography.headlineSmall)
    Spacer(modifier = Modifier.height(16.dp))
    LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        items(suggestions) { suggestion ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(suggestion.emoji, fontSize = 32.sp)
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(suggestion.themeName, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        suggestion.colorPalette.forEach { hex ->
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .background(Color(android.graphics.Color.parseColor("#$hex")), CircleShape)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text("Suggested Boards: ${suggestion.suggestedBoards.joinToString(", ")}", style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { onUseTheme(suggestion) }, modifier = Modifier.fillMaxWidth()) {
                        Text("Use This Theme")
                    }
                }
            }
        }
    }
}
