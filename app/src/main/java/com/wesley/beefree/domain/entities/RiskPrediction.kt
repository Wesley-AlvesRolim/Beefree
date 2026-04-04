package com.wesley.beefree.domain.entities

data class RiskPrediction(
    val id: Int? = null,
    val userProfileId: Int,
    val riskScore: Int,
    val riskFactors: String?,
    val notificationSent: Boolean,
    val predictedAt: Long,
)
