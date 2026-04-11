package com.wesley.beefree.domain.entities

data class UserProfileOnboardingResult(
    val id: Int? = null,
    val userProfileId: Int,
    val addictionTypeId: Int,
    val ppcsScore: Int?,
    val pgsiScore: Int?,
    val moralIncongruenceScore: Int?,
    val frequencyScore: Int?,
    val moralDisapprovalScore: Int?,
    val hasNeurodivergence: Boolean,
    val clinicalProfile: String,
    val moralIncongruenceBand: String?,
    val createdAt: Long,
)
