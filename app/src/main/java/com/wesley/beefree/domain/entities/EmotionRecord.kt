package com.wesley.beefree.domain.entities

data class EmotionRecord(
    val id: Int? = null,
    val userProfileId: Int,
    val feelingType: FeelingType,
    val intensity: Int,
    val createdAt: Long,
)

enum class FeelingType {
    ANXIETY,
    STRESS,
    LONELINESS,
    BOREDOM,
}
