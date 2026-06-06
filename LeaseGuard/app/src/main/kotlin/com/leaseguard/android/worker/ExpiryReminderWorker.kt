package com.leaseguard.android.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.leaseguard.android.R
import com.leaseguard.android.data.LeaseDatabase

class ExpiryReminderWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val leaseId = inputData.getLong("leaseId", -1)
        val daysRemaining = inputData.getInt("daysRemaining", -1)

        if (leaseId == -1L) return Result.failure()

        val db = LeaseDatabase.getDatabase(applicationContext)
        val lease = db.leaseDao().getLeaseById(leaseId) ?: return Result.success()
        val tenant = db.leaseDao().getTenantById(lease.tenant_id) ?: return Result.success()

        if (lease.status != "active") return Result.success()

        showNotification(tenant.name, daysRemaining)

        return Result.success()
    }

    private fun showNotification(tenantName: String, days: Int) {
        val channelId = "lease_expiry"
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (android.os.Build.VERSION.SDK_INT >= 26) {
            val channel = NotificationChannel(channelId, "Lease Expiry Reminders", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Lease Expiring Soon")
            .setContentText("The lease for $tenantName expires in $days days.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }
}
