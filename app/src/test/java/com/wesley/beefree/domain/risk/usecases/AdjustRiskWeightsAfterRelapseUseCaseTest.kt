package com.wesley.beefree.domain.risk.usecases

import com.wesley.beefree.domain.entities.EmotionRecord
import com.wesley.beefree.domain.entities.FeelingType
import com.wesley.beefree.domain.entities.RiskAssessment
import com.wesley.beefree.domain.entities.RiskFeatureSnapshot
import com.wesley.beefree.domain.repository.ports.MetricsRepository
import com.wesley.beefree.domain.repository.ports.RiskWeightsRepository
import com.wesley.beefree.domain.risk.RiskWeights
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AdjustRiskWeightsAfterRelapseUseCaseTest {
    private val highRiskSnapshot =
        RiskFeatureSnapshot(
            userProfileId = 1,
            sleep = 5,
            craving = 9,
            boredom = 3,
            stress = 9,
            loneliness = 5,
            fatigue = 4,
            createdAt = 0L,
        )

    @Test
    fun `adjusts weights when high stress and high craving`() =
        runTest {
            val original = RiskWeights()
            val metrics = FakeMetricsRepository(latestSnapshot = highRiskSnapshot)
            val weightsRepo = CapturingWeightsRepository(original)
            val useCase = AdjustRiskWeightsAfterRelapseUseCase(metrics, weightsRepo)

            useCase.execute(userId = 1)

            val saved = weightsRepo.savedWeights ?: error("Expected weights to be saved")
            assertTrue(saved.stress > original.stress)
            assertTrue(saved.craving > original.craving)
        }

    @Test
    fun `saved weights sum to one`() =
        runTest {
            val metrics = FakeMetricsRepository(latestSnapshot = highRiskSnapshot)
            val weightsRepo = CapturingWeightsRepository(RiskWeights())
            val useCase = AdjustRiskWeightsAfterRelapseUseCase(metrics, weightsRepo)

            useCase.execute(userId = 1)

            val saved = weightsRepo.savedWeights ?: error("Expected weights to be saved")
            val sum =
                saved.sleep + saved.craving + saved.boredom + saved.stress +
                    saved.loneliness + saved.fatigue +
                    saved.timeSinceRelapse + saved.context + saved.behavior
            assertEquals(1.0, sum, 0.0001)
        }

    @Test
    fun `does not save when no snapshot`() =
        runTest {
            val metrics = FakeMetricsRepository(latestSnapshot = null)
            val weightsRepo = CapturingWeightsRepository(RiskWeights())
            val useCase = AdjustRiskWeightsAfterRelapseUseCase(metrics, weightsRepo)

            useCase.execute(userId = 1)

            assertEquals(null, weightsRepo.savedWeights)
        }

    @Test
    fun `returns failure when repository throws`() =
        runTest {
            val metrics = FakeMetricsRepository(latestSnapshot = highRiskSnapshot, throwOnGet = true)
            val weightsRepo = CapturingWeightsRepository(RiskWeights())
            val useCase = AdjustRiskWeightsAfterRelapseUseCase(metrics, weightsRepo)

            val result = useCase.execute(userId = 1)

            assertTrue(result.isFailure)
        }

    private class FakeMetricsRepository(
        private val latestSnapshot: RiskFeatureSnapshot?,
        private val throwOnGet: Boolean = false,
    ) : MetricsRepository {
        override suspend fun insertEmotionRecord(record: EmotionRecord): Long = 0L

        override suspend fun deleteEmotionRecordsByIds(ids: List<Long>) = Unit

        override fun getEmotionRecords(userId: Int): Flow<List<EmotionRecord>> = emptyFlow()

        override fun getEmotionRecordsByType(
            userId: Int,
            feelingType: FeelingType,
        ): Flow<List<EmotionRecord>> = emptyFlow()

        override suspend fun getLatestEmotionRecord(userId: Int): EmotionRecord? = null

        override suspend fun insertRiskFeatureSnapshot(snapshot: RiskFeatureSnapshot): Long = 0L

        override fun getRiskFeatureSnapshots(userId: Int): Flow<List<RiskFeatureSnapshot>> = emptyFlow()

        override suspend fun getLatestRiskFeatureSnapshot(userId: Int): RiskFeatureSnapshot? {
            if (throwOnGet) throw RuntimeException("DB error")
            return latestSnapshot
        }

        override suspend fun insertRiskAssessment(assessment: RiskAssessment): Long = 0L

        override suspend fun deleteAllRiskAssessmentsForUser(userId: Int) = Unit

        override fun getRiskAssessments(userId: Int): Flow<List<RiskAssessment>> = emptyFlow()
    }

    private class CapturingWeightsRepository(
        private val stored: RiskWeights,
    ) : RiskWeightsRepository {
        var savedWeights: RiskWeights? = null

        override fun getWeights(userId: Int): RiskWeights = stored

        override fun saveWeights(
            userId: Int,
            weights: RiskWeights,
        ) {
            savedWeights = weights
        }
    }
}
