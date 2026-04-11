package com.wesley.beefree.domain.entities

data class DailyMicroActivityLog(
    val id: Int? = null,
    val userProfileId: Int,
    val activityId: Int,
    val dayDate: Long,
    val completedAt: Long,
)
