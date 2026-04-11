package com.wesley.beefree.domain.entities

data class DailyCheckIn(
    val id: Int? = null,
    val userProfileId: Int,
    val dopamineLevel: Int,
    val mood: String,
    val anxietyLevel: Int?,
    val checkedInAt: Long,
)
