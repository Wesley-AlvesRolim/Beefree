package com.wesley.beefree.domain.risk.ports

import com.wesley.beefree.domain.entities.RiskFeatureSnapshot
import com.wesley.beefree.domain.risk.RiskLevel
import com.wesley.beefree.domain.risk.RiskWeights

interface RiskEngine {
    fun calculateScore(
        snapshot: RiskFeatureSnapshot,
        weights: RiskWeights,
        hourOfDay: Int,
        dayOfWeek: Int,
    ): Double

    fun classify(score: Double): RiskLevel

    fun adjustWeights(
        snapshot: RiskFeatureSnapshot,
        weights: RiskWeights,
    ): RiskWeights
}
