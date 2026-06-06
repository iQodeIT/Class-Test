package com.leaseguard.android.ui.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.leaseguard.android.data.LeaseDatabase
import com.leaseguard.android.data.LeaseWithTenant
import com.leaseguard.android.ui.theme.GreenBadge
import com.leaseguard.android.ui.theme.RedBadge
import com.leaseguard.android.ui.theme.YellowBadge
import java.util.*
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    navController: NavController,
    viewModel: DashboardViewModel = viewModel(
        factory = DashboardViewModelFactory(
            LeaseDatabase.getDatabase(LocalContext.current).leaseDao()
        )
    )
) {
    val leases by viewModel.leases.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val isPro by com.leaseguard.android.util.BillingManager.getInstance(LocalContext.current).isPro.collectAsState()
    val notifyDenied by viewModel.notifyDenied.collectAsState()
    val expiringCount by viewModel.expiringCount.collectAsState()
    var showAddSheet by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            viewModel.setNotifyDenied(!isGranted)
        }
    )

    LaunchedEffect(Unit) {
        if (android.os.Build.VERSION.SDK_INT >= 33) {
            permissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("LeaseGuard", fontWeight = FontWeight.Bold) }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                if (isPro || leases.isEmpty()) {
                    showAddSheet = true
                } else {
                    navController.navigate("paywall")
                }
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add Lease")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize()) {
            if (notifyDenied && expiringCount > 0) {
                Surface(
                    color = MaterialTheme.colorScheme.errorContainer,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Notifications disabled! $expiringCount leases expiring within 90 days.",
                        modifier = Modifier.padding(12.dp),
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
            PullToRefreshBox(
                isRefreshing = isRefreshing,
                onRefresh = { viewModel.refresh() },
                modifier = Modifier.weight(1f).fillMaxWidth()
            ) {
                if (leases.isEmpty()) {
                    EmptyState(onAddClick = { showAddSheet = true })
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(leases) { leaseWithTenant ->
                            LeaseCard(leaseWithTenant, onClick = {
                                navController.navigate("detail/${leaseWithTenant.lease.id}")
                            })
                        }
                    }
                }
            }
        }
    }

    val haptic = LocalHapticFeedback.current
    val context = LocalContext.current
    if (showAddSheet) {
        ModalBottomSheet(
            onDismissRequest = { showAddSheet = false }
        ) {
            AddLeaseSheet(
                onDismiss = { showAddSheet = false },
                onSave = { tenant, lease ->
                    viewModel.saveLease(context, tenant, lease)
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    showAddSheet = false
                }
            )
        }
    }
}

@Composable
fun LeaseCard(leaseWithTenant: LeaseWithTenant, onClick: () -> Unit) {
    val lease = leaseWithTenant.lease
    val tenant = leaseWithTenant.tenant

    val daysRemaining = getDaysRemaining(lease.end_date)
    val badgeColor = when {
        daysRemaining >= 90 -> GreenBadge
        daysRemaining >= 31 -> YellowBadge
        else -> RedBadge
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = tenant.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(text = "${tenant.property_address}, Unit ${tenant.unit_number}", style = MaterialTheme.typography.bodySmall)
            }
            Surface(
                color = badgeColor,
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "$daysRemaining Days",
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun EmptyState(onAddClick: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Tap + to add your first lease", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onAddClick) {
            Text("Add First Lease")
        }
    }
}

fun getDaysRemaining(endDateMillis: Long): Long {
    val diff = endDateMillis - System.currentTimeMillis()
    return if (diff < 0) 0 else TimeUnit.MILLISECONDS.toDays(diff)
}
