package com.wesley.beefree.domain.entities

data class CognitiveThoughtRecord(
    val id: Int? = null,
    val userProfileId: Int,
    val situation: String,
    val automaticThought: String,
    val feeling: String,
    val consequence: String,
    val alternativeThought: String,
    val cognitiveDistortions: List<String>,
    val createdAt: Long,
)
