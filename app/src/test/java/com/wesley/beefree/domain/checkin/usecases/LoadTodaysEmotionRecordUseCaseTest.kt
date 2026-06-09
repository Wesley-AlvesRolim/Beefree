package com.wesley.beefree.domain.checkin.usecases

import com.wesley.beefree.domain.entities.EmotionRecord
import com.wesley.beefree.domain.entities.FeelingType
import com.wesley.beefree.domain.repository.ports.MetricsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class LoadTodaysEmotionRecordUseCaseTest {
    private val userId = 1
    private val now = System.currentTimeMillis()
    private val dayMs = 24L * 60 * 60 * 1000

    @Test
    fun `returns null when there is no emotion record today`() {
        runBlocking {
            val useCase = LoadTodaysEmotionRecordUseCase(FakeMetricsRepository(emptyList()))

            assertNull(useCase.execute(userId))
        }
    }

    @Test
    fun `returns today's latest emotion record values for all feelings`() {
        runBlocking {
            val today = now
            val records =
                listOf(
                    record(FeelingType.SLEEP, 3, today - 3_000),
                    record(FeelingType.SLEEP, 8, today - 1_000),
                    record(FeelingType.CRAVING, 4, today - 2_000),
                    record(FeelingType.BOREDOM, 2, today - 2_500),
                    record(FeelingType.STRESS, 6, today - 4_000),
                    record(FeelingType.LONELINESS, 7, today - 5_000),
                    record(FeelingType.FATIGUE, 5, today - 6_000),
                    record(FeelingType.FATIGUE, 9, today - 500),
                    record(FeelingType.CRAVING, 1, today - dayMs),
                )
            val useCase = LoadTodaysEmotionRecordUseCase(FakeMetricsRepository(records))

            val summary = useCase.execute(userId)

            assertEquals(8, summary?.getValue(FeelingType.SLEEP))
            assertEquals(4, summary?.getValue(FeelingType.CRAVING))
            assertEquals(2, summary?.getValue(FeelingType.BOREDOM))
            assertEquals(6, summary?.getValue(FeelingType.STRESS))
            assertEquals(7, summary?.getValue(FeelingType.LONELINESS))
            assertEquals(9, summary?.getValue(FeelingType.FATIGUE))
        }
    }

    private fun record(
        feelingType: FeelingType,
        intensity: Int,
        createdAt: Long,
    ) = EmotionRecord(
        userProfileId = userId,
        feelingType = feelingType,
        intensity = intensity,
        createdAt = createdAt,
    )

    private class FakeMetricsRepository(
        private val records: List<EmotionRecord>,
    ) : MetricsRepository {
        override suspend fun insertEmotionRecord(record: EmotionRecord): Long = 0L

        override fun getEmotionRecords(userId: Int): Flow<List<EmotionRecord>> = flowOf(records)

        override fun getEmotionRecordsByType(
            userId: Int,
            feelingType: FeelingType,
        ): Flow<List<EmotionRecord>> = flowOf(records.filter { it.feelingType == feelingType })

        override suspend fun getLatestEmotionRecord(userId: Int): EmotionRecord? = null

        override suspend fun deleteEmotionRecordsByIds(ids: List<Long>) = Unit

        override suspend fun insertRiskFeatureSnapshot(snapshot: com.wesley.beefree.domain.entities.RiskFeatureSnapshot): Long = 0L

        override fun getRiskFeatureSnapshots(userId: Int): Flow<List<com.wesley.beefree.domain.entities.RiskFeatureSnapshot>> =
            flowOf(emptyList())

        override suspend fun getLatestRiskFeatureSnapshot(userId: Int): com.wesley.beefree.domain.entities.RiskFeatureSnapshot? = null

        override suspend fun insertRiskAssessment(assessment: com.wesley.beefree.domain.entities.RiskAssessment): Long = 0L

        override suspend fun deleteAllRiskAssessmentsForUser(userId: Int) = Unit

        override fun getRiskAssessments(userId: Int): Flow<List<com.wesley.beefree.domain.entities.RiskAssessment>> = flowOf(emptyList())
    }
}
