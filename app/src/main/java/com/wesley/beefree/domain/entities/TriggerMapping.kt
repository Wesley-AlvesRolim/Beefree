package com.wesley.beefree.domain.entities

data class TriggerMapping(
    val id: Int? = null,
    val userProfileId: Int,
    val appPackage: String?,
    val triggerContext: String,
    val cravingIntensity: Int,
    val didRelapse: Boolean,
    val loggedAt: Long,
)
