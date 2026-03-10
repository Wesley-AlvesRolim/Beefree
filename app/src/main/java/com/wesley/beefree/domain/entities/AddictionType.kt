package com.wesley.beefree.domain.entities

data class AddictionType(
    val id: Int? = null,
    val name: String,
    val isMonitoringEnabled: Boolean = true,
    val createdAt: Long,
    val updatedAt: Long,
)
