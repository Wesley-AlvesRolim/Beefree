package com.wesley.beefree.infrastructure.events.workers

import android.content.Context
import android.content.Intent
import androidx.work.WorkerParameters
import com.wesley.beefree.MainActivity
import com.wesley.beefree.R
import com.wesley.beefree.domain.checkin.usecases.DetermineCheckInTypeUseCase
import com.wesley.beefree.domain.checkin.usecases.HasCompletedTodaysCheckInUseCase
import com.wesley.beefree.infrastructure.storage.adapters.RoomCheckInRepository
import com.wesley.beefree.infrastructure.storage.adapters.RoomUserProfileRepository
import com.wesley.beefree.infrastructure.storage.adapters.db.AppDatabase
import kotlinx.coroutines.flow.first
import java.util.Calendar

class DailyCheckInWorker(
    context: Context,
    workerParams: WorkerParameters,
) : BeeNotificationWorker(context, workerParams) {
    override fun buildNotification() =
        NotificationContent(
            id = NOTIFICATION_ID,
            channelId = CHANNEL_ID,
            title = applicationContext.getString(R.string.check_in_notification_title),
            text = applicationContext.getString(R.string.check_in_notification_body),
            intent =
                Intent(applicationContext, MainActivity::class.java).apply {
                    putExtra(EXTRA_OPEN_CHECK_IN, true)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                },
        )

    override suspend fun onTriggered(): Boolean {
        val database = AppDatabase.getDatabase(applicationContext)
        val profile =
            RoomUserProfileRepository(
                userProfileDao = database.userProfileDao(),
                userProfileAddictionDao = database.userProfileAddictionDao(),
            ).getAllProfiles()
                .first()
                .firstOrNull() ?: return true

        val hasCompleted =
            HasCompletedTodaysCheckInUseCase(
                checkInRepository = RoomCheckInRepository(database.dailyCheckInDao(), database.weeklyCheckInDao()),
                determineCheckInTypeUseCase = DetermineCheckInTypeUseCase(),
            ).execute(
                userId = profile.id ?: return true,
                userCreatedAt = profile.createdAt,
            )

        return !hasCompleted
    }

    companion object {
        const val EXTRA_OPEN_CHECK_IN = "open_check_in"
        private const val CHANNEL_ID = "daily_check_in"
        private const val NOTIFICATION_ID = 2002
        private const val WORK_NAME = "daily_check_in"
        private const val NOTIFICATION_HOUR = 21

        fun scheduleCheckInWorker(context: Context) {
            schedule(
                context = context,
                workerClass = DailyCheckInWorker::class.java,
                workName = WORK_NAME,
                channelId = CHANNEL_ID,
                channelName = "Check-in Diário",
                periodHours = 24,
                initialDelayMs = calculateDelayToNextNotification(),
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
