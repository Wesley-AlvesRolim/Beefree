package com.wesley.beefree.domain.mocks

import com.wesley.beefree.domain.repository.ports.RiskWeightsRepository
import com.wesley.beefree.domain.risk.RiskWeights

class RiskWeightsRepositoryMock(
    var weights: RiskWeights = RiskWeights(),
) : RiskWeightsRepository {
    var savedWeights: RiskWeights? = null
    val saveWeightsArgs = mutableListOf<Pair<Int, RiskWeights>>()

    override fun getWeights(userId: Int): RiskWeights = weights

    override fun saveWeights(
        userId: Int,
        weights: RiskWeights,
    ) {
        savedWeights = weights
        saveWeightsArgs += userId to weights
    }
}
