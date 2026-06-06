package com.leaseguard.android.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.leaseguard.android.data.LeaseDao
import com.leaseguard.android.data.LeaseWithTenant
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import com.leaseguard.android.data.BackupData

class SettingsViewModel(private val leaseDao: LeaseDao) : ViewModel() {

    val activeLeases = leaseDao.getActiveLeasesWithTenants()
        .map { map ->
            map.map { (lease, tenant) -> LeaseWithTenant(lease, tenant) }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun deleteAllData(onDeleted: () -> Unit) {
        viewModelScope.launch {
            leaseDao.deleteEverything()
            onDeleted()
        }
    }

    fun exportToJson(onResult: (String) -> Unit) {
        viewModelScope.launch {
            val backup = BackupData(
                tenants = leaseDao.getAllTenants(),
                leases = leaseDao.getAllLeases(),
                documents = leaseDao.getAllDocuments()
            )
            val json = Json.encodeToString(backup)
            onResult(json)
        }
    }

    fun importFromJson(json: String, onComplete: () -> Unit) {
        viewModelScope.launch {
            try {
                val backup = Json.decodeFromString<BackupData>(json)
                leaseDao.insertTenants(backup.tenants)
                leaseDao.insertLeases(backup.leases)
                leaseDao.insertDocuments(backup.documents)
                onComplete()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

class SettingsViewModelFactory(private val leaseDao: LeaseDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SettingsViewModel(leaseDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
