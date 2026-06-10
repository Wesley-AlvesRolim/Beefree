package com.wesley.beefree.infrastructure.events.workers

import android.content.Context
import android.content.Intent
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.wesley.beefree.MainActivity
import com.wesley.beefree.R
import com.wesley.beefree.domain.repository.ports.UserProfileRepository
import com.wesley.beefree.domain.risk.DefaultRiskEngine
import com.wesley.beefree.domain.risk.RiskLevel
import com.wesley.beefree.domain.risk.ports.RiskEngine
import com.wesley.beefree.domain.usecases.risk.CalculateAndSaveRiskAssessmentUseCase
import com.wesley.beefree.infrastructure.storage.adapters.KeyValueRiskWeightsRepository
import com.wesley.beefree.infrastructure.storage.adapters.RoomAddictionRepository
import com.wesley.beefree.infrastructure.storage.adapters.RoomCheckInRepository
import com.wesley.beefree.infrastructure.storage.adapters.RoomMetricsRepository
import com.wesley.beefree.infrastructure.storage.adapters.RoomUserProfileRepository
import com.wesley.beefree.infrastructure.storage.adapters.SharedPreferencesKeyValueStorage
import com.wesley.beefree.infrastructure.storage.adapters.db.AppDatabase
import kotlinx.coroutines.flow.first
import java.util.Calendar

class MidnightRiskCalculationWorker(
    context: Context,
    workerParams: WorkerParameters,
    private val userProfileRepository: UserProfileRepository,
    private val calculateAndSaveRiskAssessmentUseCase: CalculateAndSaveRiskAssessmentUseCase,
    private val riskEngine: RiskEngine = DefaultRiskEngine(),
) : BeeNotificationWorker(context, workerParams) {
    override fun buildNotification(): NotificationContent =
        NotificationContent(
            id = NOTIFICATION_ID,
            channelId = CHANNEL_ID,
            title = applicationContext.getString(R.string.midnight_risk_notification_title),
            text = applicationContext.getString(R.string.midnight_risk_notification_body),
            intent =
                Intent(applicationContext, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                },
        )

    override suspend fun onTriggered(): Boolean {
        val user = userProfileRepository.getAllProfiles().first().firstOrNull() ?: return false
        val userId = user.id ?: return false

        val scores =
            calculateAndSaveRiskAssessmentUseCase.execute(userId).getOrElse { return false }
        return scores.any {
            when (riskEngine.classify(it / 100.0)) {
                RiskLevel.MEDIUM, RiskLevel.HIGH -> true
                else -> false
            }
        }
    }

    companion object {
        private const val CHANNEL_ID = "midnight_risk"
        private const val NOTIFICATION_ID = 2004
        private const val WORK_NAME = "midnight_risk_calculation"
        private const val NOTIFICATION_HOUR = 0

        fun scheduleMidnightWorker(context: Context) {
            schedule(
                context = context,
                workerClass = MidnightRiskCalculationWorker::class.java,
                workName = WORK_NAME,
                channelId = CHANNEL_ID,
                channelName = "Análise de Risco",
                periodHours = 24,
                initialDelayMs = calculateDelayToMidnight(),
            )
        }

        private fun calculateDelayToMidnight(): Long {
            val now = Calendar.getInstance()
            val target =
                Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, NOTIFICATION_HOUR)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                    if (!after(now)) add(Calendar.DAY_OF_YEAR, 1)
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
                    if (workerClassName != MidnightRiskCalculationWorker::class.java.name) return null
                    val db = AppDatabase.getDatabase(appContext)
                    val metricsRepository =
                        RoomMetricsRepository(
                            emotionRecordDao = db.emotionRecordDao(),
                            riskFeatureSnapshotDao = db.riskFeatureSnapshotDao(),
                            riskAssessmentDao = db.riskAssessmentDao(),
                        )
                    val riskWeightsRepository =
                        KeyValueRiskWeightsRepository(SharedPreferencesKeyValueStorage(appContext))
                    val userRepository =
                        RoomUserProfileRepository(
                            userProfileDao = db.userProfileDao(),
                            userAddictionDao = db.userAddictionDao(),
                        )
                    val checkInRepository =
                        RoomCheckInRepository(
                            dailyCheckInDao = db.dailyCheckInDao(),
                            weeklyCheckInDao = db.weeklyCheckInDao(),
                        )
                    val addictionRepository =
                        RoomAddictionRepository(
                            addictionCategoryDao = db.addictionCategoryDao(),
                            relapseRecordDao = db.relapseRecordDao(),
                        )
                    return MidnightRiskCalculationWorker(
                        context = appContext,
                        workerParams = workerParameters,
                        userProfileRepository = userRepository,
                        calculateAndSaveRiskAssessmentUseCase =
                            CalculateAndSaveRiskAssessmentUseCase(
                                metricsRepository = metricsRepository,
                                riskWeightsRepository = riskWeightsRepository,
                                checkInRepository = checkInRepository,
                                addictionRepository = addictionRepository,
                            ),
                    )
                }
            }
    }
}
