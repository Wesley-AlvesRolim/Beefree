package com.wesley.beefree.infrastructure.events.workers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.wesley.beefree.MainActivity
import com.wesley.beefree.R
import java.util.Calendar
import java.util.concurrent.TimeUnit

class DailyCheckInWorker(
    context: Context,
    workerParams: WorkerParameters,
) : Worker(context, workerParams) {
    override fun doWork(): Result {
        showNotification(applicationContext)
        return Result.success()
    }

    private fun showNotification(context: Context) {
        val channelId = CHANNEL_ID
        val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val intent =
            Intent(context, MainActivity::class.java).apply {
                putExtra(EXTRA_OPEN_CHECK_IN, true)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            }

        val pendingIntent =
            PendingIntent.getActivity(
                context,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
            )

        val notification =
            NotificationCompat
                .Builder(context, channelId)
                .setSmallIcon(R.drawable.bee_mono)
                .setContentTitle(context.getString(R.string.check_in_notification_title))
                .setContentText(context.getString(R.string.check_in_notification_body))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build()

        nm.notify(NOTIFICATION_ID, notification)
    }

    companion object {
        const val EXTRA_OPEN_CHECK_IN = "open_check_in"
        private const val CHANNEL_ID = "daily_check_in"
        private const val NOTIFICATION_ID = 2002
        private const val WORK_NAME = "daily_check_in"
        private const val NOTIFICATION_HOUR = 21

        fun scheduleCheckInWorker(context: Context) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val nm = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                nm.createNotificationChannel(
                    NotificationChannel(CHANNEL_ID, "Check-in Diário", NotificationManager.IMPORTANCE_HIGH),
                )
            }
            val initialDelay = calculateDelayToNextNotification()
            val checkInWork =
                PeriodicWorkRequestBuilder<DailyCheckInWorker>(24, TimeUnit.HOURS)
                    .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
                    .build()
            WorkManager.Companion
                .getInstance(context)
                .enqueueUniquePeriodicWork(
                    WORK_NAME,
                    ExistingPeriodicWorkPolicy.KEEP,
                    checkInWork,
                )
        }

        private fun calculateDelayToNextNotification(): Long {
            val now = Calendar.getInstance()
            val target =
                Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, NOTIFICATION_HOUR)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                    if (before(now)) add(Calendar.DAY_OF_YEAR, 1)
                }
            return target.timeInMillis - now.timeInMillis
        }
    }
}
