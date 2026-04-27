package com.wesley.beefree.domain.entities

data class EmotionRecord(
    val id: Int? = null,
    val userProfileId: Int,
    val feelingType: String,
    val intensity: Int,
    val createdAt: Long,
)
