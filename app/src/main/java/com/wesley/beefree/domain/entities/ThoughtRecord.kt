package com.wesley.beefree.domain.entities

data class ThoughtRecord(
    val id: Int? = null,
    val userProfileId: Int,
    val triggerId: Int?,
    val automaticThought: String,
    val rationalResponse: String?,
    val createdAt: Long,
)
