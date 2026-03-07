package com.wesley.beefree.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.core.app.NotificationCompat
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.wesley.beefree.R
import com.wesley.beefree.infrastructure.services.AccessibilityServiceActivity
import com.wesley.beefree.utils.AccessibilityUtils
import java.util.concurrent.TimeUnit

class CheckAccessibilityWorker(
    context: Context,
    workerParams: WorkerParameters,
) : Worker(context, workerParams) {
    override fun doWork(): Result {
        val context = applicationContext
        val isEnabled =
            AccessibilityUtils.isAccessibilityServiceEnabled(
                context,
                AccessibilityServiceActivity::class.java,
            )
        if (!isEnabled) {
            showNotification(context)
        }
        return Result.success()
    }

    private fun showNotification(context: Context) {
        val channelId = "accessibility_check"
        val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(
                    channelId,
                    "Accessibility Service Check",
                    NotificationManager.IMPORTANCE_HIGH,
                )
            nm.createNotificationChannel(channel)
        }

        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        val notification =
            NotificationCompat
                .Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Enable Accessibility")
                .setContentText("Your Accessibility Service is disabled. Tap to enable.")
                .setContentIntent(
                    PendingIntent.getActivity(
                        context,
                        0,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT,
                    ),
                ).setAutoCancel(true)
                .build()

        nm.notify(2001, notification)
    }

    companion object {
        fun scheduleNotificationWorker(context: Context) {
            val checkWork =
                PeriodicWorkRequestBuilder<CheckAccessibilityWorker>(
                    15,
                    TimeUnit.MINUTES,
                ).build()
            WorkManager
                .getInstance(context)
                .enqueue(checkWork)
        }
    }
}
