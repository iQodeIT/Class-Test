package com.clearviewai.app.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.clearviewai.app.data.local.entities.MediaItem

@Composable
fun MediaCard(
    item: MediaItem,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = item.uri,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                if (item.isBlurry) {
                    Badge(containerColor = MaterialTheme.colorScheme.error) {
                        Text("Blurry", color = MaterialTheme.colorScheme.onError)
                    }
                }
                if (item.isDuplicate) {
                    Badge(containerColor = MaterialTheme.colorScheme.tertiary) {
                        Text("Duplicate", color = MaterialTheme.colorScheme.onTertiary)
                    }
                }
                item.clutterType?.let {
                    Badge(containerColor = MaterialTheme.colorScheme.secondary) {
                        Text(it, color = MaterialTheme.colorScheme.onSecondary)
                    }
                }
            }
        }
    }
}

@Composable
fun Badge(
    containerColor: androidx.compose.ui.graphics.Color,
    content: @Composable RowScope.() -> Unit
) {
    Surface(
        color = containerColor,
        shape = androidx.compose.foundation.shape.RoundedCornerShape(4.dp),
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
            content = content
        )
    }
}
