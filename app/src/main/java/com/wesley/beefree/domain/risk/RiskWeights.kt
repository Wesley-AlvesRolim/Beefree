package com.wesley.beefree.domain.risk

data class RiskWeights(
    val sleep: Double = 0.10,
    val craving: Double = 0.20,
    val boredom: Double = 0.08,
    val stress: Double = 0.15,
    val loneliness: Double = 0.10,
    val fatigue: Double = 0.07,
    val timeSinceRelapse: Double = 0.10,
    val context: Double = 0.10,
    val behavior: Double = 0.10,
)
