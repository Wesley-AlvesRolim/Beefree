package com.wesley.beefree.domain.entities

data class OnboardingScaleAnswer(
    val id: Int? = null,
    val userProfileId: Int,
    val scaleType: String,
    val questionIndex: Int,
    val answerValue: Int,
    val createdAt: Long,
)
