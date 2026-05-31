package com.wesley.beefree.domain.entities

import com.wesley.beefree.domain.risk.RiskWeights

enum class RiskTrigger {
    SLEEP,
    CRAVING,
    BOREDOM,
    STRESS,
    LONELINESS,
    FATIGUE,
    HOURS_SINCE_LAST_RELAPSE,
    HOUR_OF_DAY,
    DAY_OF_WEEK,
    TIME_SINCE_LAST_APP_OPEN,
    MISSING_CHECKINS,
}

fun RiskFeatureSnapshot.toWeightedRankedTriggers(weights: RiskWeights): List<Pair<RiskTrigger, Double>> =
    listOfNotNull(
        sleep?.let { RiskTrigger.SLEEP to it * weights.sleep },
        craving?.let { RiskTrigger.CRAVING to it * weights.craving },
        boredom?.let { RiskTrigger.BOREDOM to it * weights.boredom },
        stress?.let { RiskTrigger.STRESS to it * weights.stress },
        loneliness?.let { RiskTrigger.LONELINESS to it * weights.loneliness },
        fatigue?.let { RiskTrigger.FATIGUE to it * weights.fatigue },
        hoursSinceLastRelapse?.let {
            RiskTrigger.HOURS_SINCE_LAST_RELAPSE to it * weights.timeSinceRelapse
        },
        hourOfDay?.let { RiskTrigger.HOUR_OF_DAY to it * weights.context },
        dayOfWeek?.let { RiskTrigger.DAY_OF_WEEK to it * weights.context },
        timeSinceLastAppOpen?.let {
            RiskTrigger.TIME_SINCE_LAST_APP_OPEN to it * weights.behavior
        },
        missingCheckins?.let {
            RiskTrigger.MISSING_CHECKINS to it * weights.behavior
        },
    ).sortedByDescending { it.second }
        .take(5)
