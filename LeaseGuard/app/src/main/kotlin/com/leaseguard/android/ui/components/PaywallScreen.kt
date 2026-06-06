package com.leaseguard.android.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.leaseguard.android.MainActivity
import com.leaseguard.android.util.BillingManager

@Composable
fun PaywallScreen(onDismiss: () -> Unit) {
    val context = LocalContext.current
    val billingManager = BillingManager.getInstance(context)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Default.Lock,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text("Unlock LeaseGuard Pro", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text("Professional tools for property managers.", style = MaterialTheme.typography.bodyMedium)

        Spacer(modifier = Modifier.height(32.dp))

        FeatureItem("Unlimited Leases")
        FeatureItem("Attach Unlimited Documents")
        FeatureItem("Export Professional PDF Reports")
        FeatureItem("Priority Support")

        Spacer(modifier = Modifier.height(48.dp))

        Button(
            onClick = {
                (context as? MainActivity)?.let { billingManager.launchBillingFlow(it) }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium
        ) {
            Text("Unlock for $4.99", fontWeight = FontWeight.Bold)
        }

        TextButton(onClick = onDismiss) {
            Text("Maybe Later")
        }
    }
}

@Composable
fun FeatureItem(text: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Default.Check, contentDescription = null, tint = Color(0xFF81A172))
        Spacer(modifier = Modifier.width(12.dp))
        Text(text, style = MaterialTheme.typography.bodyLarge)
    }
}
