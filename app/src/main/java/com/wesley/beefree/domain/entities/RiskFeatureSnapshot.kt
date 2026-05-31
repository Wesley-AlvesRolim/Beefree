package com.wesley.beefree.domain.entities

data class RiskFeatureSnapshot(
    val id: Int? = null,
    val userProfileId: Int,
    val previousSnapshotId: Int? = null,
    val sleep: Int? = null,
    val craving: Int? = null,
    val boredom: Int? = null,
    val stress: Int? = null,
    val loneliness: Int? = null,
    val fatigue: Int? = null,
    val hoursSinceLastRelapse: Int? = null,
    val hourOfDay: Int? = null,
    val dayOfWeek: Int? = null,
    val timeSinceLastAppOpen: Int? = null,
    val missingCheckins: Int? = null,
    val recentIntenseUsage: Int? = null,
    val createdAt: Long,
)
