package com.leaseguard.android.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.leaseguard.android.data.Document
import com.leaseguard.android.data.LeaseDao
import com.leaseguard.android.data.LeaseWithTenant
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class LeaseDetailViewModel(private val leaseDao: LeaseDao, private val leaseId: Long) : ViewModel() {

    private val _leaseWithTenant = MutableStateFlow<LeaseWithTenant?>(null)
    val leaseWithTenant = _leaseWithTenant.asStateFlow()

    val documents = leaseDao.getDocumentsForLease(leaseId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        loadLease()
    }

    private fun loadLease() {
        viewModelScope.launch {
            val lease = leaseDao.getLeaseById(leaseId)
            if (lease != null) {
                val tenant = leaseDao.getTenantById(lease.tenant_id)
                if (tenant != null) {
                    _leaseWithTenant.value = LeaseWithTenant(lease, tenant)
                }
            }
        }
    }

    fun markAsRenewed(context: android.content.Context) {
        viewModelScope.launch {
            _leaseWithTenant.value?.let {
                val updatedLease = it.lease.copy(status = "renewed")
                leaseDao.updateLease(updatedLease)
                com.leaseguard.android.util.NotificationHelper.cancelReminders(context, leaseId)
                loadLease()
            }
        }
    }

    fun deleteLease(context: android.content.Context, onDeleted: () -> Unit) {
        viewModelScope.launch {
            leaseDao.deleteLeaseById(leaseId)
            com.leaseguard.android.util.NotificationHelper.cancelReminders(context, leaseId)
            onDeleted()
        }
    }

    fun addDocument(context: android.content.Context, name: String, type: String, uriString: String) {
        viewModelScope.launch(kotlinx.coroutines.Dispatchers.IO) {
            val uri = android.net.Uri.parse(uriString)
            val inputStream = context.contentResolver.openInputStream(uri)
            val fileName = "doc_${System.currentTimeMillis()}.jpg"
            val file = File(context.filesDir, "documents/$fileName")
            file.parentFile?.mkdirs()

            inputStream?.use { input ->
                file.outputStream().use { output ->
                    input.copyTo(output)
                }
            }

            val doc = Document(
                lease_id = leaseId,
                display_name = name,
                uri = file.absolutePath,
                doc_type = type
            )
            leaseDao.insertDocument(doc)
        }
    }
}

class LeaseDetailViewModelFactory(private val leaseDao: LeaseDao, private val leaseId: Long) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LeaseDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LeaseDetailViewModel(leaseDao, leaseId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
