package com.leaseguard.android.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.leaseguard.android.data.LeaseDao
import com.leaseguard.android.data.LeaseWithTenant
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class DashboardViewModel(private val leaseDao: LeaseDao) : ViewModel() {

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    val leases = leaseDao.getActiveLeasesWithTenants()
        .map { map ->
            map.map { (lease, tenant) -> LeaseWithTenant(lease, tenant) }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val notifyDenied = flow {
        emit(leaseDao.getAppMeta("notify_denied")?.value == "1")
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    val expiringCount = flow {
        val count = leaseDao.getExpiringLeases(System.currentTimeMillis() + 7776000000L).size // 90 days
        emit(count)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    fun setNotifyDenied(denied: Boolean) {
        viewModelScope.launch {
            leaseDao.insertAppMeta(com.leaseguard.android.data.AppMeta("notify_denied", if (denied) "1" else "0"))
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _isRefreshing.value = true
            // Local DB is already reactive via Flow, but simulate refresh
            kotlinx.coroutines.delay(500)
            _isRefreshing.value = false
        }
    }

    fun saveLease(context: android.content.Context, tenant: Tenant, lease: Lease) {
        viewModelScope.launch {
            val tenantId = leaseDao.insertTenant(tenant)
            val leaseId = leaseDao.insertLease(lease.copy(tenant_id = tenantId))
            com.leaseguard.android.util.NotificationHelper.scheduleReminders(context, leaseId, lease.end_date)
        }
    }
}

class DashboardViewModelFactory(private val leaseDao: LeaseDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DashboardViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DashboardViewModel(leaseDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
