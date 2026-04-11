package com.wesley.beefree.domain.entities

data class UserLessonProgress(
    val id: Int? = null,
    val userProfileId: Int,
    val lessonId: Int,
    val completedAt: Long,
)
