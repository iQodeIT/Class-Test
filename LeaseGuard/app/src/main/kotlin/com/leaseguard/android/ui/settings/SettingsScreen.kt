package com.leaseguard.android.ui.settings

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.leaseguard.android.data.LeaseDatabase
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = viewModel(
        factory = SettingsViewModelFactory(
            LeaseDatabase.getDatabase(LocalContext.current).leaseDao()
        )
    )
) {
    val leases by viewModel.activeLeases.collectAsState()
    val isPro by com.leaseguard.android.util.BillingManager.getInstance(LocalContext.current).isPro.collectAsState()
    var showDeleteConfirm by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Settings") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Data Management", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)

            Button(
                onClick = {
                    val summary = buildExportSummary(leases)
                    val intent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, summary)
                    }
                    context.startActivity(Intent.createChooser(intent, "Share Export"))
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.Share, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Export Data (Text Summary)")
            }

            Button(
                onClick = {
                    if (isPro) {
                        val uri = com.leaseguard.android.util.PdfGenerator.generateReport(context, leases)
                        if (uri != null) {
                            val intent = Intent(Intent.ACTION_SEND).apply {
                                type = "application/pdf"
                                putExtra(Intent.EXTRA_STREAM, uri)
                                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            }
                            context.startActivity(Intent.createChooser(intent, "Share PDF Report"))
                        }
                    } else {
                        // In a real app, I'd navigate to paywall, but here I'll show the text
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(if (isPro) Icons.Default.PictureAsPdf else Icons.Default.Lock, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(if (isPro) "Export PDF Report" else "Unlock Pro to Export")
            }

            Text("Cloud Backup", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)

            val createDocumentLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.CreateDocument("application/json"),
                onResult = { uri ->
                    uri?.let {
                        viewModel.exportToJson { json ->
                            context.contentResolver.openOutputStream(it)?.use { out ->
                                out.write(json.toByteArray())
                            }
                        }
                    }
                }
            )

            val openDocumentLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.OpenDocument(),
                onResult = { uri ->
                    uri?.let {
                        context.contentResolver.openInputStream(it)?.use { input ->
                            val json = input.bufferedReader().use { r -> r.readText() }
                            viewModel.importFromJson(json) {
                                // Reload UI or show message
                            }
                        }
                    }
                }
            )

            Button(
                onClick = {
                    if (isPro) createDocumentLauncher.launch("leaseguard_backup.json")
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(if (isPro) Icons.Default.CloudUpload else Icons.Default.Lock, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(if (isPro) "Backup to Google Drive" else "Pro: Cloud Backup")
            }

            Button(
                onClick = {
                    if (isPro) openDocumentLauncher.launch(arrayOf("application/json"))
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(if (isPro) Icons.Default.CloudDownload else Icons.Default.Lock, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(if (isPro) "Restore from Google Drive" else "Pro: Cloud Restore")
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { showDeleteConfirm = true },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Icon(Icons.Default.DeleteForever, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Delete All Data")
            }
        }
    }

    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text("Wipe All Data") },
            text = { Text("Are you sure you want to delete all tenant and lease data? This action is permanent.") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deleteAllData {
                        showDeleteConfirm = false
                    }
                }) {
                    Text("Delete Everything", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirm = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

fun buildExportSummary(leases: List<com.leaseguard.android.data.LeaseWithTenant>): String {
    val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
    val sb = StringBuilder("LeaseGuard Active Leases Export\n\n")
    leases.forEach { item ->
        sb.append("Tenant: ${item.tenant.name}\n")
        sb.append("Property: ${item.tenant.property_address}, Unit ${item.tenant.unit_number}\n")
        sb.append("Dates: ${sdf.format(Date(item.lease.start_date))} - ${sdf.format(Date(item.lease.end_date))}\n")
        sb.append("Rent: $${item.lease.monthly_rent}\n")
        sb.append("----------------------------\n")
    }
    return sb.toString()
}
