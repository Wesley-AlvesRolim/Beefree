package com.wesley.beefree.domain.repository.ports

import com.wesley.beefree.domain.risk.RiskWeights

interface RiskWeightsRepository {
    fun getWeights(userId: Int): RiskWeights

    fun saveWeights(
        userId: Int,
        weights: RiskWeights,
    )
}
