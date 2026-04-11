package com.wesley.beefree.domain.entities

data class HolisticMetrics(
    val id: Int? = null,
    val userProfileId: Int,
    val anxietyLevel: Int?,
    val emotionalSatisfaction: Int?,
    val mood: String?,
    val loggedAt: Long,
)
