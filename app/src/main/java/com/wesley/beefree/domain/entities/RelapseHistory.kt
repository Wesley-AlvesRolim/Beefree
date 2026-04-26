package com.wesley.beefree.domain.entities

data class RelapseHistory(
    val id: Int? = null,
    val addictionTypeId: Int,
    val keywordDetected: String? = null,
    val detectedText: String? = null,
    val appPackage: String? = null,
    val externalApp: String? = null,
    val wasSentToExternal: Boolean = false,
    val relapseAt: Long,
    val updatedAt: Long,
)
