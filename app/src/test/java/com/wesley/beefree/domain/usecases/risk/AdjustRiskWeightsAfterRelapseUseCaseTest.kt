package com.wesley.beefree.domain.usecases.risk

import com.wesley.beefree.domain.entities.RiskFeatureSnapshot
import com.wesley.beefree.domain.mocks.MetricsRepositoryMock
import com.wesley.beefree.domain.mocks.RiskWeightsRepositoryMock
import com.wesley.beefree.domain.risk.RiskWeights
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
            val metrics = MetricsRepositoryMock().apply { latestRiskFeatureSnapshot = highRiskSnapshot }
            val weightsRepo = RiskWeightsRepositoryMock(original)
            val useCase = AdjustRiskWeightsAfterRelapseUseCase(metrics, weightsRepo)

            useCase.execute(userId = 1)

            val saved = weightsRepo.savedWeights ?: error("Expected weights to be saved")
            assertTrue(saved.stress > original.stress)
            assertTrue(saved.craving > original.craving)
        }

    @Test
    fun `saved weights sum to one`() =
        runTest {
            val metrics = MetricsRepositoryMock().apply { latestRiskFeatureSnapshot = highRiskSnapshot }
            val weightsRepo = RiskWeightsRepositoryMock()
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
            val metrics = MetricsRepositoryMock()
            val weightsRepo = RiskWeightsRepositoryMock()
            val useCase = AdjustRiskWeightsAfterRelapseUseCase(metrics, weightsRepo)

            useCase.execute(userId = 1)

            assertEquals(null, weightsRepo.savedWeights)
        }

    @Test
    fun `returns failure when repository throws`() =
        runTest {
            val metrics =
                MetricsRepositoryMock().apply {
                    latestRiskFeatureSnapshot = highRiskSnapshot
                    throwOnGetLatestRiskFeatureSnapshot = RuntimeException("DB error")
                }
            val weightsRepo = RiskWeightsRepositoryMock()
            val useCase = AdjustRiskWeightsAfterRelapseUseCase(metrics, weightsRepo)

            val result = useCase.execute(userId = 1)

            assertTrue(result.isFailure)
        }
}
