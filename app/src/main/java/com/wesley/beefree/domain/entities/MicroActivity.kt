package com.wesley.beefree.domain.entities

data class MicroActivity(
    val id: Int? = null,
    val addictionTypeId: Int?,
    val activityType: String,
    val activityName: String,
    val createdAt: Long,
)
