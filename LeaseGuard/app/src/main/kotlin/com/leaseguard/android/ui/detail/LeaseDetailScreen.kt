package com.leaseguard.android.ui.detail

import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.leaseguard.android.data.LeaseDatabase
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaseDetailScreen(
    leaseId: Long,
    navController: NavController,
    viewModel: LeaseDetailViewModel = viewModel(
        factory = LeaseDetailViewModelFactory(
            LeaseDatabase.getDatabase(LocalContext.current).leaseDao(),
            leaseId
        ),
        key = leaseId.toString()
    )
) {
    val leaseWithTenant by viewModel.leaseWithTenant.collectAsState()
    val isPro by com.leaseguard.android.util.BillingManager.getInstance(LocalContext.current).isPro.collectAsState()
    var showDeleteConfirm by remember { mutableStateOf(false) }
    var showAttachDialog by remember { mutableStateOf(false) }
    var showTypeDialog by remember { mutableStateOf(false) }
    var capturedUri by remember { mutableStateOf<Uri?>(null) }

    val dateFormatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lease Detail") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { showDeleteConfirm = true }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete")
                    }
                }
            )
        }
    ) { padding ->
        leaseWithTenant?.let { data ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Tenant Info
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Tenant Information", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        DetailRow("Name", data.tenant.name)
                        DetailRow("Address", data.tenant.property_address)
                        DetailRow("Unit", data.tenant.unit_number)
                        DetailRow("Email", data.tenant.email)
                        DetailRow("Phone", data.tenant.phone)
                    }
                }

                // Lease Info
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Lease Information", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        DetailRow("Start Date", dateFormatter.format(Date(data.lease.start_date)))
                        DetailRow("End Date", dateFormatter.format(Date(data.lease.end_date)))
                        DetailRow("Monthly Rent", "$${data.lease.monthly_rent}")
                        DetailRow("Status", data.lease.status.replaceFirstChar { it.uppercase() })
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                if (data.lease.status == "active") {
                    Button(
                        onClick = { viewModel.markAsRenewed(context) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Mark as Renewed")
                    }
                }

                // Document section
                val documents by viewModel.documents.collectAsState()

                Text("Documents", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)

                if (documents.isEmpty()) {
                    Text("No documents attached", style = MaterialTheme.typography.bodySmall)
                } else {
                    documents.forEach { doc ->
                        DocumentItem(doc)
                    }
                }

                Button(
                    onClick = {
                        if (isPro) showAttachDialog = true else navController.navigate("paywall")
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (!isPro) {
                        Icon(Icons.Default.Lock, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Pro Required")
                    } else {
                        Icon(Icons.Default.AttachFile, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Attach Document")
                    }
                }
            }
        }
    }

    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text("Delete Lease") },
            text = { Text("Are you sure you want to delete this lease? This action cannot be undone.") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deleteLease(context) {
                        navController.popBackStack()
                    }
                    showDeleteConfirm = false
                }) {
                    Text("Delete", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirm = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    if (showAttachDialog) {
        AttachDocumentDialog(
            onDismiss = { showAttachDialog = false },
            onDocumentCaptured = { uri ->
                capturedUri = uri
                showAttachDialog = false
                showTypeDialog = true
            }
        )
    }

    val context = LocalContext.current
    if (showTypeDialog) {
        DocumentTypeDialog(
            onDismiss = { showTypeDialog = false },
            onConfirm = { name, type ->
                capturedUri?.let { viewModel.addDocument(context, name, type, it.toString()) }
                showTypeDialog = false
            }
        )
    }
}

@Composable
fun DocumentItem(doc: com.leaseguard.android.data.Document) {
    val icon = when (doc.doc_type) {
        "lease_agreement" -> Icons.Default.Description
        "inspection" -> Icons.Default.CheckCircle
        "receipt" -> Icons.Default.Receipt
        else -> Icons.Default.InsertDriveFile
    }

    ListItem(
        headlineContent = { Text(doc.display_name) },
        supportingContent = { Text(doc.doc_type.replace("_", " ").replaceFirstChar { it.uppercase() }) },
        leadingContent = { Icon(icon, contentDescription = null) }
    )
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Text(text = "$label:", modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
        Text(text = if (value.isBlank()) "-" else value, modifier = Modifier.weight(2f), style = MaterialTheme.typography.bodyMedium)
    }
}
