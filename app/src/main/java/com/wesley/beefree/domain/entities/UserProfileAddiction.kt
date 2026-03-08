package com.wesley.beefree.domain.entities

data class UserProfileAddiction(
    val id: Int? = null,
    val userProfileId: Int,
    val addictionTypeId: Int,
    val createdAt: Long,
)
