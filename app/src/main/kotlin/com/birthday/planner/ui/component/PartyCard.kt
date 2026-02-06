package com.birthday.planner.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.birthday.planner.data.model.Party
import com.birthday.planner.data.model.PartyType
import com.birthday.planner.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun PartyCard(
    party: Party,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val gradient = when (party.partyType) {
        PartyType.KIDS -> Brush.linearGradient(listOf(KidsPrimary, SunsetOrange))
        PartyType.TEENS -> Brush.linearGradient(listOf(TeensPrimary, TeensSecondary))
        PartyType.ADULT -> Brush.linearGradient(listOf(AdultsPrimary, AdultsSecondary))
        else -> Brush.linearGradient(listOf(Color.Gray, Color.DarkGray))
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(160.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradient)
                .padding(20.dp)
        ) {
            Column(modifier = Modifier.align(Alignment.TopStart)) {
                Text(
                    text = party.partyTitle,
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${party.birthdayPersonName}'s ${party.age}th Birthday",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }

            Column(modifier = Modifier.align(Alignment.BottomStart)) {
                val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                Text(
                    text = sdf.format(party.partyDate),
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.White
                )
            }

            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "${party.guestCount} guests",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.White
                )
            }
        }
    }
}
