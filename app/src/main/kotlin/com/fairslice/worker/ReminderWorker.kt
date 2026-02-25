package com.fairslice.worker

import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.fairslice.R

class ReminderWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val billName = inputData.getString("billName") ?: "A bill"

        showNotification(billName)

        return Result.success()
    }

    private fun showNotification(billName: String) {
        val builder = NotificationCompat.Builder(applicationContext, "fairslice_reminders")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("FairSlice Reminder 💸")
            .setContentText("Hey! Just a nudge — your payment for $billName is due today.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        try {
            with(NotificationManagerCompat.from(applicationContext)) {
                notify(System.currentTimeMillis().toInt(), builder.build())
            }
        } catch (e: SecurityException) {
            // Permission not granted
        }
    }
}
