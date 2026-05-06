package com.wesley.beefree.domain.entities

import com.wesley.beefree.domain.onboarding.TreatmentProfile

data class HelpInterventionSession(
    val id: Int? = null,
    val userProfileId: Int,
    val intensityBefore: Int,
    val intensityAfter: Int,
    val bodyLocations: List<String>,
    val clinicalBranch: TreatmentProfile,
    val selectedValue: String? = null,
    val directionAnswer: String? = null,
    val committedAction: String? = null,
    val automaticThought: String? = null,
    val alternativeThought: String? = null,
    val wasImpulseStillStrong: Boolean,
    val surgeSurfLoopCount: Int,
    val createdAt: Long,
)
