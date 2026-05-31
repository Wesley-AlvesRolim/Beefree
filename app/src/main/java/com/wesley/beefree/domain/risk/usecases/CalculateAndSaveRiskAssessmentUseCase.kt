package com.wesley.beefree.domain.risk.usecases

import com.wesley.beefree.domain.entities.DailyCheckIn
import com.wesley.beefree.domain.entities.EmotionRecord
import com.wesley.beefree.domain.entities.FeelingType
import com.wesley.beefree.domain.entities.RelapseRecord
import com.wesley.beefree.domain.entities.RiskAssessment
import com.wesley.beefree.domain.entities.RiskFeatureSnapshot
import com.wesley.beefree.domain.repository.ports.AddictionRepository
import com.wesley.beefree.domain.repository.ports.CheckInRepository
import com.wesley.beefree.domain.repository.ports.MetricsRepository
import com.wesley.beefree.domain.repository.ports.RiskWeightsRepository
import com.wesley.beefree.domain.risk.DefaultRiskEngine
import com.wesley.beefree.domain.risk.ports.RiskEngine
import kotlinx.coroutines.flow.firstOrNull
import java.util.Calendar
import kotlin.math.exp
import kotlin.math.roundToInt

class CalculateAndSaveRiskAssessmentUseCase(
    private val metricsRepository: MetricsRepository,
    private val riskWeightsRepository: RiskWeightsRepository,
    private val checkInRepository: CheckInRepository? = null,
    private val addictionRepository: AddictionRepository? = null,
    private val riskEngine: RiskEngine = DefaultRiskEngine(),
) {
    suspend fun execute(userId: Int): Result<List<Int>> =
        runCatching {
            val baseSnapshot =
                metricsRepository.getLatestRiskFeatureSnapshot(userId)
                    ?: return@runCatching emptyList()

            val now = System.currentTimeMillis()

            val emotionHistory = metricsRepository.getEmotionRecords(userId).firstOrNull().orEmpty()
            val checkInHistory = checkInRepository?.getDailyCheckIns(userId)?.firstOrNull().orEmpty()
            val relapseHistory = addictionRepository?.getRelapseHistory()?.firstOrNull().orEmpty()

            val enrichedSnapshot =
                enrichSnapshotFromHistory(
                    baseSnapshot = baseSnapshot,
                    emotions = emotionHistory,
                    checkIns = checkInHistory,
                    relapses = relapseHistory,
                    now = now,
                )

            val weights = riskWeightsRepository.getWeights(userId)

            metricsRepository.deleteAllRiskAssessmentsForUser(userId)

            buildList {
                repeat(HOURS_IN_DAY) { offset ->
                    val targetMs = now + offset * MILLIS_PER_HOUR
                    val calendar = Calendar.getInstance().apply { timeInMillis = targetMs }
                    val hourOfDay = calendar.get(Calendar.HOUR_OF_DAY)
                    val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

                    val projectedSnapshot =
                        enrichedSnapshot.copy(
                            hoursSinceLastRelapse =
                                enrichedSnapshot.hoursSinceLastRelapse?.plus(offset),
                        )

                    val score = riskEngine.calculateScore(projectedSnapshot, weights, hourOfDay, dayOfWeek)
                    val riskScore = (score * PERCENT_SCALE).toInt().coerceIn(0, 100)
                    add(riskScore)

                    metricsRepository.insertRiskAssessment(
                        RiskAssessment(
                            userProfileId = userId,
                            riskScore = riskScore,
                            timeWindowStart = targetMs,
                            createdAt = now,
                        ),
                    )
                }
            }
        }

    private fun enrichSnapshotFromHistory(
        baseSnapshot: RiskFeatureSnapshot,
        emotions: List<EmotionRecord>,
        checkIns: List<DailyCheckIn>,
        relapses: List<RelapseRecord>,
        now: Long,
    ): RiskFeatureSnapshot {
        val emotionCutoff = now - EMOTION_HORIZON_DAYS * MILLIS_PER_DAY
        val recentEmotions = emotions.filter { it.createdAt >= emotionCutoff }
        val derivedEmotion = derivedEmotionByType(recentEmotions, now)

        val mostRecentRelapseMs = relapses.maxOfOrNull { it.createdAt }
        val hoursSinceLastRelapse =
            mostRecentRelapseMs
                ?.let { ((now - it).coerceAtLeast(0L) / MILLIS_PER_HOUR).toInt() }
                ?: baseSnapshot.hoursSinceLastRelapse

        val missingCheckins =
            if (checkIns.isEmpty()) {
                baseSnapshot.missingCheckins
            } else {
                missingCheckinsInLastDays(checkIns, now)
            }

        return baseSnapshot.copy(
            sleep = derivedEmotion[FeelingType.SLEEP] ?: baseSnapshot.sleep,
            craving = derivedEmotion[FeelingType.CRAVING] ?: baseSnapshot.craving,
            boredom = derivedEmotion[FeelingType.BOREDOM] ?: baseSnapshot.boredom,
            stress = derivedEmotion[FeelingType.STRESS] ?: baseSnapshot.stress,
            loneliness = derivedEmotion[FeelingType.LONELINESS] ?: baseSnapshot.loneliness,
            fatigue = derivedEmotion[FeelingType.FATIGUE] ?: baseSnapshot.fatigue,
            hoursSinceLastRelapse = hoursSinceLastRelapse,
            missingCheckins = missingCheckins,
        )
    }

    private fun derivedEmotionByType(
        records: List<EmotionRecord>,
        now: Long,
    ): Map<FeelingType, Int> {
        if (records.isEmpty()) return emptyMap()
        return records
            .groupBy { it.feelingType }
            .mapValues { (_, list) -> weightedAverageIntensity(list, now) }
    }

    private fun weightedAverageIntensity(
        records: List<EmotionRecord>,
        now: Long,
    ): Int {
        var weightedSum = 0.0
        var weightTotal = 0.0
        records.forEach { record ->
            val ageDays = (now - record.createdAt).coerceAtLeast(0L) / MILLIS_PER_DAY.toDouble()
            val weight = exp(-ageDays / DECAY_DAYS)
            weightedSum += record.intensity * weight
            weightTotal += weight
        }
        return if (weightTotal == 0.0) 0 else (weightedSum / weightTotal).roundToInt()
    }

    private fun missingCheckinsInLastDays(
        checkIns: List<DailyCheckIn>,
        now: Long,
    ): Int {
        val cutoff = now - CHECKIN_HORIZON_DAYS * MILLIS_PER_DAY
        val daysWithCheckIn =
            checkIns
                .asSequence()
                .filter { it.checkedInAt in cutoff..now }
                .map { ((now - it.checkedInAt) / MILLIS_PER_DAY).toInt() }
                .toSet()
        return (CHECKIN_HORIZON_DAYS - daysWithCheckIn.size).coerceIn(0, CHECKIN_HORIZON_DAYS)
    }

    companion object {
        private const val HOURS_IN_DAY = 24
        private const val MILLIS_PER_HOUR = 3_600_000L
        private const val MILLIS_PER_DAY = 86_400_000L
        private const val EMOTION_HORIZON_DAYS = 14
        private const val DECAY_DAYS = 7.0
        private const val CHECKIN_HORIZON_DAYS = 7
        private const val PERCENT_SCALE = 100
    }
}
