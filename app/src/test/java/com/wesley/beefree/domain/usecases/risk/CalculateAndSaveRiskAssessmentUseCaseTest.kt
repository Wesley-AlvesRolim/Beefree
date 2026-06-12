package com.wesley.beefree.domain.usecases.risk

import com.wesley.beefree.domain.entities.DailyCheckIn
import com.wesley.beefree.domain.entities.EmotionRecord
import com.wesley.beefree.domain.entities.FeelingType
import com.wesley.beefree.domain.entities.RelapseRecord
import com.wesley.beefree.domain.entities.RiskFeatureSnapshot
import com.wesley.beefree.domain.mocks.AddictionRepositoryMock
import com.wesley.beefree.domain.mocks.CheckInRepositoryMock
import com.wesley.beefree.domain.mocks.MetricsRepositoryMock
import com.wesley.beefree.domain.mocks.RiskWeightsRepositoryMock
import com.wesley.beefree.domain.onboarding.TreatmentProfile
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CalculateAndSaveRiskAssessmentUseCaseTest {
    private val defaultSnapshot =
        RiskFeatureSnapshot(
            userProfileId = 1,
            sleep = 3,
            craving = 2,
            boredom = 1,
            stress = 3,
            loneliness = 2,
            fatigue = 2,
            hoursSinceLastRelapse = 24,
            hourOfDay = 10,
            dayOfWeek = 3,
            missingCheckins = 0,
            recentIntenseUsage = 0,
            createdAt = 0L,
        )

    @Test
    fun `produces 24 assessments when snapshot exists`() =
        runTest {
            val metrics = MetricsRepositoryMock().apply { latestRiskFeatureSnapshot = defaultSnapshot }
            val weights = RiskWeightsRepositoryMock()
            val useCase = CalculateAndSaveRiskAssessmentUseCase(metrics, weights)

            val result = useCase.execute(userId = 1)

            assertTrue(result.isSuccess)
            assertEquals(24, result.getOrThrow().size)
        }

    @Test
    fun `deletes old assessments before inserting`() =
        runTest {
            val metrics = MetricsRepositoryMock().apply { latestRiskFeatureSnapshot = defaultSnapshot }
            val weights = RiskWeightsRepositoryMock()
            val useCase = CalculateAndSaveRiskAssessmentUseCase(metrics, weights)

            useCase.execute(userId = 1)

            assertTrue(metrics.deleteAllRiskAssessmentsCalled)
        }

    @Test
    fun `inserts exactly 24 assessments into repository`() =
        runTest {
            val metrics = MetricsRepositoryMock().apply { latestRiskFeatureSnapshot = defaultSnapshot }
            val weights = RiskWeightsRepositoryMock()
            val useCase = CalculateAndSaveRiskAssessmentUseCase(metrics, weights)

            useCase.execute(userId = 1)

            assertEquals(24, metrics.insertRiskAssessmentCount)
        }

    @Test
    fun `returns empty list when no snapshot`() =
        runTest {
            val metrics = MetricsRepositoryMock()
            val weights = RiskWeightsRepositoryMock()
            val useCase = CalculateAndSaveRiskAssessmentUseCase(metrics, weights)

            val result = useCase.execute(userId = 1)

            assertTrue(result.isSuccess)
            assertTrue(result.getOrThrow().isEmpty())
        }

    @Test
    fun `all scores are in range 0 to 100`() =
        runTest {
            val metrics = MetricsRepositoryMock().apply { latestRiskFeatureSnapshot = defaultSnapshot }
            val weights = RiskWeightsRepositoryMock()
            val useCase = CalculateAndSaveRiskAssessmentUseCase(metrics, weights)

            val scores = useCase.execute(userId = 1).getOrThrow()

            assertTrue(scores.all { it in 0..100 })
        }

    @Test
    fun `returns failure when repository throws`() =
        runTest {
            val metrics =
                MetricsRepositoryMock().apply {
                    latestRiskFeatureSnapshot = defaultSnapshot
                    throwOnInsertRiskAssessment = RuntimeException("DB error")
                }
            val weights = RiskWeightsRepositoryMock()
            val useCase = CalculateAndSaveRiskAssessmentUseCase(metrics, weights)

            val result = useCase.execute(userId = 1)

            assertTrue(result.isFailure)
        }

    @Test
    fun `succeeds when optional repositories are null`() =
        runTest {
            val metrics = MetricsRepositoryMock().apply { latestRiskFeatureSnapshot = defaultSnapshot }
            val weights = RiskWeightsRepositoryMock()
            val useCase =
                CalculateAndSaveRiskAssessmentUseCase(
                    metricsRepository = metrics,
                    riskWeightsRepository = weights,
                    checkInRepository = null,
                    addictionRepository = null,
                )

            val result = useCase.execute(userId = 1)

            assertTrue(result.isSuccess)
            assertEquals(24, result.getOrThrow().size)
        }

    @Test
    fun `uses latest relapse from history to derive hoursSinceLastRelapse`() =
        runTest {
            val now = System.currentTimeMillis()
            val sixHoursAgo = now - 6 * MILLIS_PER_HOUR
            val quietSnapshot = quietSnapshot(hoursSinceLastRelapse = 500)
            val metrics = MetricsRepositoryMock().apply { latestRiskFeatureSnapshot = quietSnapshot }
            val weights = RiskWeightsRepositoryMock()
            val addictions =
                AddictionRepositoryMock().apply {
                    relapseHistory =
                        listOf(
                            relapse(createdAt = now - 10 * MILLIS_PER_DAY),
                            relapse(createdAt = sixHoursAgo),
                        )
                }
            val useCase =
                CalculateAndSaveRiskAssessmentUseCase(
                    metricsRepository = metrics,
                    riskWeightsRepository = weights,
                    addictionRepository = addictions,
                )

            val resultEnriched = useCase.execute(userId = 1).getOrThrow()

            val metricsNoHistory = MetricsRepositoryMock().apply { latestRiskFeatureSnapshot = quietSnapshot }
            val resultBaseline =
                CalculateAndSaveRiskAssessmentUseCase(metricsNoHistory, weights)
                    .execute(userId = 1)
                    .getOrThrow()

            assertNotEquals(resultBaseline, resultEnriched)
        }

    @Test
    fun `derives missingCheckins from 7-day check-in history`() =
        runTest {
            val now = System.currentTimeMillis()
            val metrics = MetricsRepositoryMock().apply { latestRiskFeatureSnapshot = defaultSnapshot.copy(missingCheckins = 0) }
            val weights = RiskWeightsRepositoryMock()
            val checkIns =
                CheckInRepositoryMock().apply {
                    dailyCheckIns =
                        listOf(
                            dailyCheckIn(checkedInAt = now - 1 * MILLIS_PER_DAY),
                            dailyCheckIn(checkedInAt = now - 2 * MILLIS_PER_DAY),
                        )
                }
            val useCase =
                CalculateAndSaveRiskAssessmentUseCase(
                    metricsRepository = metrics,
                    riskWeightsRepository = weights,
                    checkInRepository = checkIns,
                )

            val result = useCase.execute(userId = 1)

            assertTrue(result.isSuccess)
            assertEquals(24, result.getOrThrow().size)
        }

    @Test
    fun `applies weighted emotion history to override snapshot emotion fields`() =
        runTest {
            val now = System.currentTimeMillis()
            val snapshot = quietSnapshot(craving = 1)
            val recentHighCraving =
                List(5) {
                    EmotionRecord(
                        userProfileId = 1,
                        feelingType = FeelingType.CRAVING,
                        intensity = 5,
                        createdAt = now - it * MILLIS_PER_DAY,
                    )
                }
            val metrics =
                MetricsRepositoryMock().apply {
                    latestRiskFeatureSnapshot = snapshot
                    emotionRecords = recentHighCraving
                }
            val weights = RiskWeightsRepositoryMock()
            val useCase = CalculateAndSaveRiskAssessmentUseCase(metrics, weights)

            val enrichedResult = useCase.execute(userId = 1).getOrThrow()

            val metricsNoEmotions = MetricsRepositoryMock().apply { latestRiskFeatureSnapshot = snapshot }
            val baselineResult =
                CalculateAndSaveRiskAssessmentUseCase(metricsNoEmotions, weights)
                    .execute(userId = 1)
                    .getOrThrow()

            assertNotEquals(baselineResult, enrichedResult)
        }

    @Test
    fun `concentrates emotion impact on the hour it historically occurs`() =
        runTest {
            val now = System.currentTimeMillis()
            val snapshot = quietSnapshot()
            val stressAtSameHour =
                List(3) {
                    EmotionRecord(
                        userProfileId = 1,
                        feelingType = FeelingType.STRESS,
                        intensity = 9,
                        createdAt = now - it * MILLIS_PER_DAY,
                    )
                }
            val metrics =
                MetricsRepositoryMock().apply {
                    latestRiskFeatureSnapshot = snapshot
                    emotionRecords = stressAtSameHour
                }
            val weights = RiskWeightsRepositoryMock()

            val scores = CalculateAndSaveRiskAssessmentUseCase(metrics, weights).execute(userId = 1).getOrThrow()

            val baseline =
                CalculateAndSaveRiskAssessmentUseCase(
                    MetricsRepositoryMock().apply { latestRiskFeatureSnapshot = snapshot },
                    weights,
                ).execute(userId = 1).getOrThrow()

            assertTrue(scores[0] > baseline[0])
            assertEquals(baseline[1], scores[1])
        }

    private fun quietSnapshot(
        craving: Int = 0,
        hoursSinceLastRelapse: Int? = null,
    ) = RiskFeatureSnapshot(
        userProfileId = 1,
        sleep = 0,
        craving = craving,
        boredom = 0,
        stress = 0,
        loneliness = 0,
        fatigue = 0,
        hoursSinceLastRelapse = hoursSinceLastRelapse,
        hourOfDay = 10,
        dayOfWeek = 3,
        missingCheckins = 0,
        recentIntenseUsage = 0,
        createdAt = 0L,
    )

    private fun relapse(createdAt: Long) =
        RelapseRecord(
            addictionCategoryId = 1,
            createdAt = createdAt,
        )

    private fun dailyCheckIn(checkedInAt: Long) =
        DailyCheckIn(
            userProfileId = 1,
            treatmentProfile = TreatmentProfile.TCC,
            answers = emptyMap(),
            checkedInAt = checkedInAt,
        )

    companion object {
        private const val MILLIS_PER_HOUR = 3_600_000L
        private const val MILLIS_PER_DAY = 86_400_000L
    }
}
