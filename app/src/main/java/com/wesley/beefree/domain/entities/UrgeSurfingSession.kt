package com.wesley.beefree.domain.entities

data class UrgeSurfingSession(
    val id: Int? = null,
    val userProfileId: Int,
    val interventionId: Int?,
    val peakIntensity: Int?,
    val durationSeconds: Int?,
    val completed: Boolean,
    val loggedAt: Long,
)
