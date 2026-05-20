package com.wesley.beefree.infrastructure.events.workers

import android.content.Context
import android.content.Intent
import androidx.work.WorkerParameters
import com.wesley.beefree.MainActivity
import com.wesley.beefree.R
import com.wesley.beefree.infrastructure.storage.adapters.RoomRiskFeatureSnapshotRepository
import com.wesley.beefree.infrastructure.storage.adapters.RoomUserProfileRepository
import com.wesley.beefree.infrastructure.storage.adapters.db.AppDatabase
import kotlinx.coroutines.flow.first
import kotlin.collections.firstOrNull

class EmotionalRecordReminderWorker(
    context: Context,
    workerParams: WorkerParameters,
) : BeeNotificationWorker(context, workerParams) {
    override fun buildNotification(): NotificationContent =
        NotificationContent(
            id = NOTIFICATION_ID,
            channelId = CHANNEL_ID,
            title = applicationContext.getString(R.string.emotional_record_notification_title),
            text = applicationContext.getString(R.string.emotional_record_notification_body),
            intent =
                Intent(applicationContext, MainActivity::class.java).apply {
                    putExtra(EXTRA_OPEN_EMOTIONAL_RECORD, true)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                },
        )

    override suspend fun onTriggered(): Boolean {
        val database = AppDatabase.getDatabase(applicationContext)
        val userRepository =
            RoomUserProfileRepository(
                userProfileDao = database.userProfileDao(),
                userAddictionDao = database.userAddictionDao(),
            )
        val user = userRepository.getAllProfiles().first().firstOrNull() ?: return false
        val userId = user.id ?: return false

        val repository = RoomRiskFeatureSnapshotRepository(database.riskFeatureSnapshotDao())
        val latest = repository.getLatestByUser(userId)

        val lastEventTimeMs = latest?.createdAt ?: user.createdAt
        val notifyAtMs = lastEventTimeMs + NOTIFICATION_INTERVAL_MS
        val nowMs = System.currentTimeMillis()

        return notifyAtMs <= nowMs
    }

    companion object {
        const val EXTRA_OPEN_EMOTIONAL_RECORD = "open_emotional_record"
        private const val CHANNEL_ID = "emotional_record_reminder"
        private const val NOTIFICATION_ID = 2003
        private const val WORK_NAME = "emotional_record_reminder"
        private const val NOTIFICATION_HOURLY = 3L
        private const val NOTIFICATION_INTERVAL_MS = NOTIFICATION_HOURLY * 60 * 60 * 1000L

        fun scheduleEmotionalRecordWorker(context: Context) {
            schedule(
                context = context,
                workerClass = EmotionalRecordReminderWorker::class.java,
                workName = WORK_NAME,
                channelId = CHANNEL_ID,
                channelName = "Registro Emocional",
                periodHours = NOTIFICATION_HOURLY,
                initialDelayMs = NOTIFICATION_INTERVAL_MS,
            )
        }
    }
}
