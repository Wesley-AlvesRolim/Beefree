package com.wesley.beefree.domain.entities

data class DailyLesson(
    val id: Int? = null,
    val title: String,
    val contentBody: String,
    val targetProfile: String?,
    val createdAt: Long,
)
