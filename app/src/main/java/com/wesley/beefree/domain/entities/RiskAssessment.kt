package com.wesley.beefree.domain.entities

data class RiskAssessment(
    val id: Int? = null,
    val userProfileId: Int,
    val riskScore: Int,
    val timeWindowStart: Long? = null,
    val createdAt: Long,
)
