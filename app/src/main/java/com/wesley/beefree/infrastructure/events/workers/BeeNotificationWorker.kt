package com.wesley.beefree.infrastructure.events.workers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.wesley.beefree.R
import java.util.concurrent.TimeUnit

abstract class BeeNotificationWorker(
    context: Context,
    workerParams: WorkerParameters,
) : CoroutineWorker(context, workerParams) {
    data class NotificationContent(
        val id: Int,
        val channelId: String,
        val title: String,
        val text: String,
        val intent: Intent,
    )

    abstract fun buildNotification(): NotificationContent

    open suspend fun onTriggered(): Boolean = true

    final override suspend fun doWork(): Result {
        if (!onTriggered()) return Result.success()
        post(buildNotification())
        return Result.success()
    }

    private fun post(content: NotificationContent) {
        val nm = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val pendingIntent =
            PendingIntent.getActivity(
                applicationContext,
                0,
                content.intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
            )
        val notification =
            NotificationCompat
                .Builder(applicationContext, content.channelId)
                .setSmallIcon(R.drawable.bee_mono)
                .setContentTitle(content.title)
                .setContentText(content.text)
                .setStyle(NotificationCompat.BigTextStyle().bigText(content.text))
                .setColor(ContextCompat.getColor(applicationContext, R.color.notification_accent))
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .setVisibility(NotificationCompat.VISIBILITY_PRIVATE)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build()
        nm.notify(content.id, notification)
    }

    companion object {
        fun schedule(
            context: Context,
            workerClass: Class<out BeeNotificationWorker>,
            workName: String,
            channelId: String,
            channelName: String,
            periodHours: Long,
            initialDelayMs: Long,
            policy: ExistingPeriodicWorkPolicy = ExistingPeriodicWorkPolicy.KEEP,
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                nm.createNotificationChannel(
                    NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH),
                )
            }
            val work =
                PeriodicWorkRequest
                    .Builder(workerClass, periodHours, TimeUnit.HOURS)
                    .setInitialDelay(initialDelayMs, TimeUnit.MILLISECONDS)
                    .build()
            WorkManager.getInstance(context).enqueueUniquePeriodicWork(workName, policy, work)
        }
    }
}
