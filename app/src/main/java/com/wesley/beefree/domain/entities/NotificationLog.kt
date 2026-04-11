package com.wesley.beefree.domain.entities

data class NotificationLog(
    val id: Int? = null,
    val userProfileId: Int,
    val notificationType: String,
    val contentRefId: Int?,
    val sentAt: Long,
    val openedAt: Long?,
)
