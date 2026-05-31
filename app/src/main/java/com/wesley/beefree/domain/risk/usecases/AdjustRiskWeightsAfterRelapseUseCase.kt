package com.wesley.beefree.domain.risk.usecases

import com.wesley.beefree.domain.repository.ports.MetricsRepository
import com.wesley.beefree.domain.repository.ports.RiskWeightsRepository
import com.wesley.beefree.domain.risk.DefaultRiskEngine
import com.wesley.beefree.domain.risk.ports.RiskEngine

class AdjustRiskWeightsAfterRelapseUseCase(
    private val metricsRepository: MetricsRepository,
    private val riskWeightsRepository: RiskWeightsRepository,
    private val riskEngine: RiskEngine = DefaultRiskEngine(),
) {
    suspend fun execute(userId: Int): Result<Unit> =
        runCatching {
            val snapshot = metricsRepository.getLatestRiskFeatureSnapshot(userId) ?: return@runCatching
            val weights = riskWeightsRepository.getWeights(userId)
            val adjusted = riskEngine.adjustWeights(snapshot, weights)
            riskWeightsRepository.saveWeights(userId, adjusted)
        }
}
