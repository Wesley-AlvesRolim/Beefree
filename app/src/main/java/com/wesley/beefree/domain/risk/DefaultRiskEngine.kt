package com.wesley.beefree.domain.risk

import com.wesley.beefree.domain.entities.RiskFeatureSnapshot
import com.wesley.beefree.domain.risk.ports.RiskEngine
import com.wesley.beefree.domain.risk.ports.RiskThresholds
import kotlin.math.exp
import kotlin.math.min

class DefaultRiskEngine(
    private val thresholds: RiskThresholds = DefaultRiskThresholds(),
) : RiskEngine {
    override fun calculateScore(
        snapshot: RiskFeatureSnapshot,
        weights: RiskWeights,
        hourOfDay: Int,
        dayOfWeek: Int,
    ): Double {
        var score = 0.0

        val sleep = (snapshot.sleep ?: 0) / MAX_EMOTION_INTENSITY
        val craving = (snapshot.craving ?: 0) / MAX_EMOTION_INTENSITY
        val boredom = (snapshot.boredom ?: 0) / MAX_EMOTION_INTENSITY
        val stress = (snapshot.stress ?: 0) / MAX_EMOTION_INTENSITY
        val loneliness = (snapshot.loneliness ?: 0) / MAX_EMOTION_INTENSITY
        val fatigue = (snapshot.fatigue ?: 0) / MAX_EMOTION_INTENSITY
        val hoursSinceLastRelapse = snapshot.hoursSinceLastRelapse?.toDouble() ?: 0.0
        val missingCheckins = snapshot.missingCheckins ?: 0
        val recentIntenseUsage = snapshot.recentIntenseUsage ?: 0

        score += sleep * weights.sleep
        score += craving * weights.craving
        score += boredom * weights.boredom
        score += stress * weights.stress
        score += loneliness * weights.loneliness
        score += fatigue * weights.fatigue

        score +=
            calculateTimeBasedCravingScore(
                hoursSinceLastRelapse,
                weights.timeSinceRelapse,
            )

        if (hourOfDay in 0..5) score += weights.context
        if (dayOfWeek >= 6) score += weights.context * 0.5

        if (missingCheckins > 0) score += weights.behavior
        if (recentIntenseUsage > 0) score += weights.behavior

        return min(score, 1.0)
    }

    override fun classify(score: Double): RiskLevel =
        when {
            score * PERCENT_SCALE < thresholds.mediumRisk -> RiskLevel.LOW
            score * PERCENT_SCALE < thresholds.highRisk -> RiskLevel.MEDIUM
            else -> RiskLevel.HIGH
        }

    override fun adjustWeights(
        snapshot: RiskFeatureSnapshot,
        weights: RiskWeights,
    ): RiskWeights {
        val craving = snapshot.craving ?: 0
        val stress = snapshot.stress ?: 0
        val loneliness = snapshot.loneliness ?: 0
        val fatigue = snapshot.fatigue ?: 0
        val sleep = snapshot.sleep ?: 0

        val adjusted =
            weights.copy(
                craving = weights.craving + if (craving > HIGH_EMOTION_THRESHOLD) CRAVING_WEIGHT_BOOST else 0.0,
                stress = weights.stress + if (stress > HIGH_EMOTION_THRESHOLD) EMOTION_WEIGHT_BOOST else 0.0,
                loneliness = weights.loneliness + if (loneliness > HIGH_EMOTION_THRESHOLD) EMOTION_WEIGHT_BOOST else 0.0,
                fatigue = weights.fatigue + if (fatigue > HIGH_EMOTION_THRESHOLD) EMOTION_WEIGHT_BOOST else 0.0,
                sleep = weights.sleep + if (sleep > HIGH_EMOTION_THRESHOLD) EMOTION_WEIGHT_BOOST else 0.0,
            )
        return normalize(adjusted)
    }

    private fun calculateTimeBasedCravingScore(
        hoursSinceLastRelapse: Double,
        cravingWeight: Double,
    ): Double {
        val hoursPerDay = 24.0
        val daysSinceLastRelapse =
            hoursSinceLastRelapse / hoursPerDay

        val cravingPeakDay = 30.0
        val cravingEndsAfterDay = 90.0

        if (daysSinceLastRelapse >= cravingEndsAfterDay) {
            return 0.0
        }

        val cravingIntensity =
            daysSinceLastRelapse *
                exp(-daysSinceLastRelapse / cravingPeakDay)

        val fadeOutMultiplier =
            1.0 - (daysSinceLastRelapse / cravingEndsAfterDay)

        val adjustedCravingIntensity =
            cravingIntensity * fadeOutMultiplier

        return adjustedCravingIntensity * cravingWeight
    }

    private fun normalize(weights: RiskWeights): RiskWeights {
        val sum =
            weights.sleep +
                weights.craving +
                weights.boredom +
                weights.stress +
                weights.loneliness +
                weights.fatigue +
                weights.timeSinceRelapse +
                weights.context +
                weights.behavior
        if (sum == 0.0) return weights
        return weights.copy(
            sleep = weights.sleep / sum,
            craving = weights.craving / sum,
            boredom = weights.boredom / sum,
            stress = weights.stress / sum,
            loneliness = weights.loneliness / sum,
            fatigue = weights.fatigue / sum,
            timeSinceRelapse = weights.timeSinceRelapse / sum,
            context = weights.context / sum,
            behavior = weights.behavior / sum,
        )
    }

    private companion object {
        const val MAX_EMOTION_INTENSITY = 10.0
        const val HIGH_EMOTION_THRESHOLD = 7
        const val CRAVING_WEIGHT_BOOST = 0.015
        const val EMOTION_WEIGHT_BOOST = 0.01
        const val PERCENT_SCALE = 100.0
    }
}
