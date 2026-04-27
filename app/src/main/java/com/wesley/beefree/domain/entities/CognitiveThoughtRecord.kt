package com.wesley.beefree.domain.entities

data class CognitiveThoughtRecord(
    val id: Int? = null,
    val userProfileId: Int,
    val automaticThought: String,
    val rationalResponse: String? = null,
    val createdAt: Long,
)
