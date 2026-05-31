package com.wesley.beefree.domain.risk.usecases

import com.wesley.beefree.domain.entities.AddictionCategory
import com.wesley.beefree.domain.entities.DailyCheckIn
import com.wesley.beefree.domain.entities.EmotionRecord
import com.wesley.beefree.domain.entities.FeelingType
import com.wesley.beefree.domain.entities.RelapseRecord
import com.wesley.beefree.domain.entities.RiskAssessment
import com.wesley.beefree.domain.entities.RiskFeatureSnapshot
import com.wesley.beefree.domain.entities.WeeklyCheckIn
import com.wesley.beefree.domain.onboarding.TreatmentProfile
import com.wesley.beefree.domain.repository.ports.AddictionRepository
import com.wesley.beefree.domain.repository.ports.CheckInRepository
import com.wesley.beefree.domain.repository.ports.MetricsRepository
import com.wesley.beefree.domain.repository.ports.RiskWeightsRepository
import com.wesley.beefree.domain.risk.RiskWeights
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
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
            val metrics = FakeMetricsRepository(latestSnapshot = defaultSnapshot)
            val weights = FakeRiskWeightsRepository()
            val useCase = CalculateAndSaveRiskAssessmentUseCase(metrics, weights)

            val result = useCase.execute(userId = 1)

            assertTrue(result.isSuccess)
            assertEquals(24, result.getOrThrow().size)
        }

    @Test
    fun `deletes old assessments before inserting`() =
        runTest {
            val metrics = FakeMetricsRepository(latestSnapshot = defaultSnapshot)
            val weights = FakeRiskWeightsRepository()
            val useCase = CalculateAndSaveRiskAssessmentUseCase(metrics, weights)

            useCase.execute(userId = 1)

            assertTrue(metrics.deleteAllCalled)
        }

    @Test
    fun `inserts exactly 24 assessments into repository`() =
        runTest {
            val metrics = FakeMetricsRepository(latestSnapshot = defaultSnapshot)
            val weights = FakeRiskWeightsRepository()
            val useCase = CalculateAndSaveRiskAssessmentUseCase(metrics, weights)

            useCase.execute(userId = 1)

            assertEquals(24, metrics.insertedAssessments.size)
        }

    @Test
    fun `returns empty list when no snapshot`() =
        runTest {
            val metrics = FakeMetricsRepository(latestSnapshot = null)
            val weights = FakeRiskWeightsRepository()
            val useCase = CalculateAndSaveRiskAssessmentUseCase(metrics, weights)

            val result = useCase.execute(userId = 1)

            assertTrue(result.isSuccess)
            assertTrue(result.getOrThrow().isEmpty())
        }

    @Test
    fun `all scores are in range 0 to 100`() =
        runTest {
            val metrics = FakeMetricsRepository(latestSnapshot = defaultSnapshot)
            val weights = FakeRiskWeightsRepository()
            val useCase = CalculateAndSaveRiskAssessmentUseCase(metrics, weights)

            val scores = useCase.execute(userId = 1).getOrThrow()

            assertTrue(scores.all { it in 0..100 })
        }

    @Test
    fun `returns failure when repository throws`() =
        runTest {
            val metrics = FakeMetricsRepository(latestSnapshot = defaultSnapshot, throwOnInsert = true)
            val weights = FakeRiskWeightsRepository()
            val useCase = CalculateAndSaveRiskAssessmentUseCase(metrics, weights)

            val result = useCase.execute(userId = 1)

            assertTrue(result.isFailure)
        }

    @Test
    fun `succeeds when optional repositories are null`() =
        runTest {
            val metrics = FakeMetricsRepository(latestSnapshot = defaultSnapshot)
            val weights = FakeRiskWeightsRepository()
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
            val metrics = FakeMetricsRepository(latestSnapshot = quietSnapshot)
            val weights = FakeRiskWeightsRepository()
            val addictions =
                FakeAddictionRepository(
                    relapses =
                        listOf(
                            relapse(createdAt = now - 10 * MILLIS_PER_DAY),
                            relapse(createdAt = sixHoursAgo),
                        ),
                )
            val useCase =
                CalculateAndSaveRiskAssessmentUseCase(
                    metricsRepository = metrics,
                    riskWeightsRepository = weights,
                    addictionRepository = addictions,
                )

            val resultEnriched = useCase.execute(userId = 1).getOrThrow()

            val metricsNoHistory = FakeMetricsRepository(latestSnapshot = quietSnapshot)
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
            val metrics = FakeMetricsRepository(latestSnapshot = defaultSnapshot.copy(missingCheckins = 0))
            val weights = FakeRiskWeightsRepository()
            val checkIns =
                FakeCheckInRepository(
                    dailyCheckIns =
                        listOf(
                            dailyCheckIn(checkedInAt = now - 1 * MILLIS_PER_DAY),
                            dailyCheckIn(checkedInAt = now - 2 * MILLIS_PER_DAY),
                        ),
                )
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
                FakeMetricsRepository(
                    latestSnapshot = snapshot,
                    emotionRecords = recentHighCraving,
                )
            val weights = FakeRiskWeightsRepository()
            val useCase = CalculateAndSaveRiskAssessmentUseCase(metrics, weights)

            val enrichedResult = useCase.execute(userId = 1).getOrThrow()

            val metricsNoEmotions = FakeMetricsRepository(latestSnapshot = snapshot)
            val baselineResult =
                CalculateAndSaveRiskAssessmentUseCase(metricsNoEmotions, weights)
                    .execute(userId = 1)
                    .getOrThrow()

            assertNotEquals(baselineResult, enrichedResult)
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

    private class FakeMetricsRepository(
        private val latestSnapshot: RiskFeatureSnapshot?,
        private val throwOnInsert: Boolean = false,
        private val emotionRecords: List<EmotionRecord> = emptyList(),
    ) : MetricsRepository {
        val insertedAssessments = mutableListOf<RiskAssessment>()
        var deleteAllCalled = false

        override suspend fun insertEmotionRecord(record: EmotionRecord): Long = 0L

        override suspend fun deleteEmotionRecordsByIds(ids: List<Long>) = Unit

        override fun getEmotionRecords(userId: Int): Flow<List<EmotionRecord>> = flowOf(emotionRecords)

        override fun getEmotionRecordsByType(
            userId: Int,
            feelingType: FeelingType,
        ): Flow<List<EmotionRecord>> = flowOf(emotionRecords.filter { it.feelingType == feelingType })

        override suspend fun getLatestEmotionRecord(userId: Int): EmotionRecord? = emotionRecords.maxByOrNull { it.createdAt }

        override suspend fun insertRiskFeatureSnapshot(snapshot: RiskFeatureSnapshot): Long = 0L

        override fun getRiskFeatureSnapshots(userId: Int): Flow<List<RiskFeatureSnapshot>> = emptyFlow()

        override suspend fun getLatestRiskFeatureSnapshot(userId: Int): RiskFeatureSnapshot? = latestSnapshot

        override suspend fun insertRiskAssessment(assessment: RiskAssessment): Long {
            if (throwOnInsert) throw RuntimeException("DB error")
            insertedAssessments.add(assessment)
            return insertedAssessments.size.toLong()
        }

        override suspend fun deleteAllRiskAssessmentsForUser(userId: Int) {
            deleteAllCalled = true
        }

        override fun getRiskAssessments(userId: Int): Flow<List<RiskAssessment>> = emptyFlow()
    }

    private class FakeRiskWeightsRepository : RiskWeightsRepository {
        override fun getWeights(userId: Int): RiskWeights = RiskWeights()

        override fun saveWeights(
            userId: Int,
            weights: RiskWeights,
        ) = Unit
    }

    private class FakeCheckInRepository(
        private val dailyCheckIns: List<DailyCheckIn> = emptyList(),
    ) : CheckInRepository {
        override suspend fun insertDailyCheckIn(checkIn: DailyCheckIn): Long = 0L

        override fun getDailyCheckIns(userId: Int): Flow<List<DailyCheckIn>> = flowOf(dailyCheckIns)

        override suspend fun insertWeeklyCheckIn(checkIn: WeeklyCheckIn): Long = 0L

        override fun getWeeklyCheckIns(userId: Int): Flow<List<WeeklyCheckIn>> = emptyFlow()
    }

    private class FakeAddictionRepository(
        private val relapses: List<RelapseRecord> = emptyList(),
    ) : AddictionRepository {
        override suspend fun insertAddictionCategory(category: AddictionCategory): Long = 0L

        override suspend fun updateAddictionCategory(category: AddictionCategory) = Unit

        override suspend fun deleteAddictionCategory(category: AddictionCategory) = Unit

        override suspend fun getAddictionCategoryById(id: Int): AddictionCategory? = null

        override fun getAllAddictionCategories(): Flow<List<AddictionCategory>> = emptyFlow()

        override suspend fun insertRelapse(relapse: RelapseRecord): Long = 0L

        override fun getRelapseHistory(): Flow<List<RelapseRecord>> = flowOf(relapses)
    }

    companion object {
        private const val MILLIS_PER_HOUR = 3_600_000L
        private const val MILLIS_PER_DAY = 86_400_000L
    }
}
