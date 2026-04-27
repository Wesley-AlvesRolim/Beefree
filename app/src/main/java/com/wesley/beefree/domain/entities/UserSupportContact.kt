package com.wesley.beefree.domain.entities

data class UserSupportContact(
    val id: Int? = null,
    val userProfileId: Int,
    val name: String,
    val phoneNumber: String,
    val isActive: Boolean = true,
    val createdAt: Long,
)
