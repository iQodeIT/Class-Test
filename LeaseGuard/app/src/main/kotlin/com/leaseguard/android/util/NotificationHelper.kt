package com.leaseguard.android.util

import android.content.Context
import androidx.work.*
import com.leaseguard.android.worker.ExpiryReminderWorker
import java.util.concurrent.TimeUnit

object NotificationHelper {
    fun scheduleReminders(context: Context, leaseId: Long, endDate: Long) {
        val now = System.currentTimeMillis()
        val days90 = endDate - TimeUnit.DAYS.toMillis(90)
        val days60 = endDate - TimeUnit.DAYS.toMillis(60)
        val days30 = endDate - TimeUnit.DAYS.toMillis(30)

        if (days90 > now) schedule(context, leaseId, 90, days90 - now)
        if (days60 > now) schedule(context, leaseId, 60, days60 - now)
        if (days30 > now) schedule(context, leaseId, 30, days30 - now)
    }

    private fun schedule(context: Context, leaseId: Long, days: Int, delay: Long) {
        val data = Data.Builder()
            .putLong("leaseId", leaseId)
            .putInt("daysRemaining", days)
            .build()

        val request = OneTimeWorkRequestBuilder<ExpiryReminderWorker>()
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .setInputData(data)
            .addTag("lease_$leaseId")
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            "lease_${leaseId}_$days",
            ExistingWorkPolicy.REPLACE,
            request
        )
    }

    fun cancelReminders(context: Context, leaseId: Long) {
        WorkManager.getInstance(context).cancelAllWorkByTag("lease_$leaseId")
    }
}
