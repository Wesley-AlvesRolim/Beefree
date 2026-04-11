package com.wesley.beefree.domain.entities

data class InterventionLog(
    val id: Int? = null,
    val userProfileId: Int,
    val interventionType: String,
    val triggeredBy: String,
    val wasCompleted: Boolean,
    val createdAt: Long,
)
