package com.wesley.beefree.infrastructure.events.workers

import android.content.Context
import android.content.Intent
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.wesley.beefree.MainActivity
import com.wesley.beefree.R
import com.wesley.beefree.domain.repository.ports.MetricsRepository
import com.wesley.beefree.domain.repository.ports.OnboardingRepository
import com.wesley.beefree.domain.repository.ports.UserProfileRepository
import com.wesley.beefree.domain.risk.DefaultRiskEngine
import com.wesley.beefree.domain.risk.RiskLevel
import com.wesley.beefree.domain.risk.ports.RiskEngine
import com.wesley.beefree.infrastructure.storage.adapters.RoomMetricsRepository
import com.wesley.beefree.infrastructure.storage.adapters.RoomOnboardingRepository
import com.wesley.beefree.infrastructure.storage.adapters.RoomUserProfileRepository
import com.wesley.beefree.infrastructure.storage.adapters.db.AppDatabase
import kotlinx.coroutines.flow.first
import java.util.Calendar
import kotlin.collections.randomOrNull

class HourlyRiskHobbyReminderWorker(
    context: Context,
    workerParams: WorkerParameters,
    private val userProfileRepository: UserProfileRepository,
    private val metricsRepository: MetricsRepository,
    private val onboardingRepository: OnboardingRepository,
    private val riskEngine: RiskEngine = DefaultRiskEngine(),
) : BeeNotificationWorker(context, workerParams) {
    private var suggestedHobbyName: String? = null

    override fun buildNotification(): NotificationContent {
        val hobbyName = suggestedHobbyName
        val bodyText =
            if (hobbyName.isNullOrBlank()) {
                applicationContext.getString(R.string.hourly_risk_notification_body_generic)
            } else {
                applicationContext.getString(R.string.hourly_risk_notification_body_with_hobby, hobbyName)
            }

        return NotificationContent(
            id = NOTIFICATION_ID,
            channelId = CHANNEL_ID,
            title = applicationContext.getString(R.string.hourly_risk_notification_title),
            text = bodyText,
            intent =
                Intent(applicationContext, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                },
        )
    }

    override suspend fun onTriggered(): Boolean {
        val user = userProfileRepository.getAllProfiles().first().firstOrNull() ?: return false
        val userId = user.id ?: return false

        val targetHour = Calendar.getInstance().apply { add(Calendar.HOUR_OF_DAY, 1) }
        val riskAssessment =
            metricsRepository
                .getRiskAssessments(userId)
                .first()
                .firstOrNull { assessment ->
                    assessment.timeWindow
                        ?.toLongOrNull()
                        ?.let { timeWindowMs -> matchesHour(calendarFromMillis(timeWindowMs), targetHour) }
                        ?: false
                }
                ?: return false

        if (riskEngine.classify(riskAssessment.riskScore / 100.0) != RiskLevel.HIGH) return false

        suggestedHobbyName =
            onboardingRepository
                .getHobbies(userId)
                .first()
                .randomOrNull()
                ?.hobbyName
        return true
    }

    companion object {
        private const val CHANNEL_ID = "hourly_risk_hobby_reminder"
        private const val NOTIFICATION_ID = 2005
        private const val WORK_NAME = "hourly_risk_hobby_reminder"
        private const val NOTIFICATION_MINUTE = 59

        fun scheduleHourlyRiskHobbyReminderWorker(context: Context) {
            schedule(
                context = context,
                workerClass = HourlyRiskHobbyReminderWorker::class.java,
                workName = WORK_NAME,
                channelId = CHANNEL_ID,
                channelName = context.getString(R.string.hourly_risk_notification_channel_name),
                periodHours = 1,
                initialDelayMs = calculateDelayToNextHourReminder(),
            )
        }

        private fun calculateDelayToNextHourReminder(): Long {
            val now = Calendar.getInstance()
            val target =
                Calendar.getInstance().apply {
                    set(Calendar.MINUTE, NOTIFICATION_MINUTE)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                    if (!after(now)) add(Calendar.HOUR_OF_DAY, 1)
                }
            return target.timeInMillis - now.timeInMillis
        }

        fun factory(context: Context): WorkerFactory =
            object : WorkerFactory() {
                override fun createWorker(
                    appContext: Context,
                    workerClassName: String,
                    workerParameters: WorkerParameters,
                ): ListenableWorker? {
                    if (workerClassName != HourlyRiskHobbyReminderWorker::class.java.name) return null
                    val db = AppDatabase.getDatabase(appContext)
                    val metricsRepository =
                        RoomMetricsRepository(
                            emotionRecordDao = db.emotionRecordDao(),
                            riskFeatureSnapshotDao = db.riskFeatureSnapshotDao(),
                            riskAssessmentDao = db.riskAssessmentDao(),
                        )
                    val userRepository =
                        RoomUserProfileRepository(
                            userProfileDao = db.userProfileDao(),
                            userAddictionDao = db.userAddictionDao(),
                        )
                    val onboardingRepository =
                        RoomOnboardingRepository(
                            onboardingSessionDao = db.userOnboardingSessionDao(),
                            coreValueDao = db.userCoreValueDao(),
                            hobbyDao = db.userHobbyDao(),
                            objectiveDao = db.userObjectiveDao(),
                            symptomDao = db.userSymptomDao(),
                        )
                    return HourlyRiskHobbyReminderWorker(
                        context = appContext,
                        workerParams = workerParameters,
                        userProfileRepository = userRepository,
                        metricsRepository = metricsRepository,
                        onboardingRepository = onboardingRepository,
                    )
                }
            }
    }
}

private fun calendarFromMillis(timeInMillis: Long): Calendar = Calendar.getInstance().apply { this.timeInMillis = timeInMillis }

private fun matchesHour(
    first: Calendar,
    second: Calendar,
): Boolean =
    first.get(Calendar.YEAR) == second.get(Calendar.YEAR) &&
        first.get(Calendar.DAY_OF_YEAR) == second.get(Calendar.DAY_OF_YEAR) &&
        first.get(Calendar.HOUR_OF_DAY) == second.get(Calendar.HOUR_OF_DAY)
