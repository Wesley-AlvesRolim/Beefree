package com.wesley.beefree.domain.entities

data class RelapseRecord(
    val id: Int? = null,
    val addictionCategoryId: Int,
    val keywordDetected: String? = null,
    val detectedText: String? = null,
    val appUsageSessionId: Int? = null,
    val createdAt: Long,
)
