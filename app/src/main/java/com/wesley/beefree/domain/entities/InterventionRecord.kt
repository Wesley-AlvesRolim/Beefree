package com.wesley.beefree.domain.entities

data class InterventionRecord(
    val id: Int? = null,
    val userProfileId: Int,
    val interventionType: String,
    val triggerType: String,
    val wasCompleted: Boolean,
    val createdAt: Long,
)
