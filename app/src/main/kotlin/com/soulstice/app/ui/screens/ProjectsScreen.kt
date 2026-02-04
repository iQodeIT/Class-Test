package com.soulstice.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.soulstice.app.ui.components.SoulsticeCard
import com.soulstice.app.ui.components.SoulsticeProgressBar
import com.soulstice.app.ui.theme.Sage100
import com.soulstice.app.ui.theme.Sage700
import com.soulstice.app.ui.theme.Taupe500
import com.soulstice.app.ui.theme.Taupe900

data class MockProject(val id: String, val name: String, val progress: Float, val status: String)

@Composable
fun ProjectsScreen() {
    val projects = listOf(
        MockProject("1", "Soulstice App", 0.2f, "Active"),
        MockProject("2", "Home Renovation", 0.6f, "Active"),
        MockProject("3", "Book Writing", 0.0f, "Active")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text("Projects", style = MaterialTheme.typography.headlineLarge, color = Taupe900)

        Spacer(modifier = Modifier.height(24.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            items(projects) { project ->
                SoulsticeCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(project.name, style = MaterialTheme.typography.titleMedium, color = Taupe900)
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(Sage100)
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(project.status, color = Sage700, fontSize = 10.sp)
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        SoulsticeProgressBar(progress = project.progress)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("${(project.progress * 100).toInt()}% Complete", color = Taupe500, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}
