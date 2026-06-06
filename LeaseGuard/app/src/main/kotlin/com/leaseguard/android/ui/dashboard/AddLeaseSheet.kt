package com.leaseguard.android.ui.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.leaseguard.android.data.Lease
import com.leaseguard.android.data.Tenant
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddLeaseSheet(
    onDismiss: () -> Unit,
    onSave: (Tenant, Lease) -> Unit
) {
    var step by remember { mutableIntStateOf(1) }

    // Step 1: Tenant Info
    var name by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var unit by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }

    // Step 2: Lease Info
    var startDate by remember { mutableLongStateOf(System.currentTimeMillis()) }
    var endDate by remember { mutableLongStateOf(System.currentTimeMillis() + 31536000000L) } // +1 year
    var rent by remember { mutableStateOf("") }

    var showError by remember { mutableStateOf(false) }
    var isSaving by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .navigationBarsPadding()
            .imePadding()
    ) {
        Text(
            text = if (step == 1) "Step 1: Tenant Details" else "Step 2: Lease Details",
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(16.dp))

        if (step == 1) {
            TenantStep(
                name, { name = it },
                address, { address = it },
                unit, { unit = it },
                email, { email = it },
                phone, { phone = it },
                showError
            )
        } else {
            LeaseStep(
                startDate, { startDate = it },
                endDate, { endDate = it },
                rent, { rent = it },
                showError
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            if (step == 2) {
                TextButton(onClick = { step = 1 }) {
                    Text("Back")
                }
            }
            Button(
                onClick = {
                    if (step == 1) {
                        if (name.isBlank() || address.isBlank()) {
                            showError = true
                        } else {
                            showError = false
                            step = 2
                        }
                    } else {
                        if (rent.isBlank()) {
                            showError = true
                        } else {
                            isSaving = true
                            val tenant = Tenant(name = name, email = email, phone = phone, property_address = address, unit_number = unit)
                            val lease = Lease(tenant_id = 0, start_date = startDate, end_date = endDate, monthly_rent = rent.toDoubleOrNull() ?: 0.0)
                            onSave(tenant, lease)
                        }
                    }
                },
                enabled = !isSaving
            ) {
                if (isSaving) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Text(if (step == 1) "Next" else "Save Lease")
                }
            }
        }
    }
}

@Composable
fun TenantStep(
    name: String, onNameChange: (String) -> Unit,
    address: String, onAddressChange: (String) -> Unit,
    unit: String, onUnitChange: (String) -> Unit,
    email: String, onEmailChange: (String) -> Unit,
    phone: String, onPhoneChange: (String) -> Unit,
    showError: Boolean
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedTextField(
            value = name, onValueChange = onNameChange, label = { Text("Tenant Name *") },
            modifier = Modifier.fillMaxWidth(), isError = showError && name.isBlank()
        )
        OutlinedTextField(
            value = address, onValueChange = onAddressChange, label = { Text("Property Address *") },
            modifier = Modifier.fillMaxWidth(), isError = showError && address.isBlank()
        )
        OutlinedTextField(
            value = unit, onValueChange = onUnitChange, label = { Text("Unit Number") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = email, onValueChange = onEmailChange, label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )
        OutlinedTextField(
            value = phone, onValueChange = onPhoneChange, label = { Text("Phone") },
            modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaseStep(
    startDate: Long, onStartChange: (Long) -> Unit,
    endDate: Long, onEndChange: (Long) -> Unit,
    rent: String, onRentChange: (String) -> Unit,
    showError: Boolean
) {
    val dateFormatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())

    var showStartPicker by remember { mutableStateOf(false) }
    var showEndPicker by remember { mutableStateOf(false) }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedCard(onClick = { showStartPicker = true }, modifier = Modifier.fillMaxWidth()) {
            Row(modifier = Modifier.padding(16.dp)) {
                Text("Start Date: ${dateFormatter.format(Date(startDate))}", modifier = Modifier.weight(1f))
            }
        }
        OutlinedCard(onClick = { showEndPicker = true }, modifier = Modifier.fillMaxWidth()) {
            Row(modifier = Modifier.padding(16.dp)) {
                Text("End Date: ${dateFormatter.format(Date(endDate))}", modifier = Modifier.weight(1f))
            }
        }
        OutlinedTextField(
            value = rent, onValueChange = onRentChange, label = { Text("Monthly Rent *") },
            modifier = Modifier.fillMaxWidth(), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            isError = showError && rent.isBlank()
        )

        if (showStartPicker) {
            val datePickerState = rememberDatePickerState(initialSelectedDateMillis = startDate)
            DatePickerDialog(
                onDismissRequest = { showStartPicker = false },
                confirmButton = {
                    TextButton(onClick = {
                        datePickerState.selectedDateMillis?.let { onStartChange(it) }
                        showStartPicker = false
                    }) { Text("OK") }
                }
            ) { DatePicker(state = datePickerState) }
        }

        if (showEndPicker) {
            val datePickerState = rememberDatePickerState(initialSelectedDateMillis = endDate)
            DatePickerDialog(
                onDismissRequest = { showEndPicker = false },
                confirmButton = {
                    TextButton(onClick = {
                        datePickerState.selectedDateMillis?.let { onEndChange(it) }
                        showEndPicker = false
                    }) { Text("OK") }
                }
            ) { DatePicker(state = datePickerState) }
        }
    }
}
