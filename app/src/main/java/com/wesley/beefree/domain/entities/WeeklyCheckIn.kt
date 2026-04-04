package com.wesley.beefree.domain.entities

data class WeeklyCheckIn(
    val id: Int? = null,
    val userProfileId: Int,
    val weekStartDate: Long,
    val valuesAlignmentText: String?,
    val realConnectionEnergy: Int?,
    val createdAt: Long,
)
