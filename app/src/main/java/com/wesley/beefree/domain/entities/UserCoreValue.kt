package com.wesley.beefree.domain.entities

data class UserCoreValue(
    val id: Int? = null,
    val userProfileId: Int,
    val valueName: String,
    val createdAt: Long,
)
