package com.wesley.beefree.domain.entities

data class UserOnboardingSession(
    val id: Int? = null,
    val userProfileId: Int,
    val clinicalApproach: String,
    val ppcsScore: Int?,
    val pgsiScore: Int?,
    val moralIncongruenceScore: Int?,
    val frequencyScore: Int?,
    val moralDisapprovalScore: Int?,
    val hasNeurodivergence: Boolean,
    val createdAt: Long,
)
