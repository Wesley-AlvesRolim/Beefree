package com.wesley.beefree.domain.entities

data class UserProfile(
    val id: Int? = null,
    val profileName: String,
    val gender: String? = null,
    val createdAt: Long,
    val updatedAt: Long,
)
